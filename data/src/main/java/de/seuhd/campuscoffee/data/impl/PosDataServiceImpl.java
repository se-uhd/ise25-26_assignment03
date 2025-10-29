package de.seuhd.campuscoffee.data.impl;

import de.seuhd.campuscoffee.data.mapper.PosEntityMapper;
import de.seuhd.campuscoffee.data.persistence.AddressEntity;
import de.seuhd.campuscoffee.data.persistence.PosEntity;
import de.seuhd.campuscoffee.data.persistence.PosRepository;
import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.exceptions.DuplicatePosNameException;
import de.seuhd.campuscoffee.domain.exceptions.PosNotFoundException;
import de.seuhd.campuscoffee.domain.ports.PosDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the POS data service that the domain layer provides as a port.
 * This layer is responsible for data access and persistence.
 * Business logic should be in the service layer.
 */
@Service
@RequiredArgsConstructor
class PosDataServiceImpl implements PosDataService {
    private final PosRepository posRepository;
    private final PosEntityMapper posEntityMapper;

    @Override
    public void clear() {
        posRepository.deleteAllInBatch();
        posRepository.flush();
        posRepository.resetSequence();
    }

    @Override
    @NonNull
    public List<Pos> getAll() {
        return posRepository.findAll().stream()
                .map(posEntityMapper::fromEntity)
                .toList();
    }

    @Override
    @NonNull
    public Pos getById(@NonNull Long id) throws PosNotFoundException {
        return posRepository.findById(id)
                .map(posEntityMapper::fromEntity)
                .orElseThrow(() -> new PosNotFoundException(id));
    }

    @Override
    @NonNull
    public Pos upsert(@NonNull Pos pos) {
        // Map POS domain object to entity and save
        try {
            if (pos.getId() == null) {
                // Create new POS
                return posEntityMapper.fromEntity(
                        posRepository.saveAndFlush(posEntityMapper.toEntity(pos))
                );
            }

            // Update existing POS
            PosEntity posEntity = posRepository.findById(pos.getId())
                    .orElseThrow(() -> new PosNotFoundException(pos.getId()));

            // Map incoming data to entity
            PosEntity mappedPosEntity = posEntityMapper.toEntity(pos);

            // Update entity fields
            posEntity.setName(mappedPosEntity.getName());
            posEntity.setDescription(mappedPosEntity.getDescription());
            posEntity.setType(mappedPosEntity.getType());
            posEntity.setCampus(mappedPosEntity.getCampus());
            // Note: timestamps are managed by JPA lifecycle callbacks (@PreUpdate)

            // Update address fields
            AddressEntity addressEntity = posEntity.getAddress();
            AddressEntity mappedAddress = mappedPosEntity.getAddress();
            addressEntity.setStreet(mappedAddress.getStreet());
            addressEntity.setHouseNumber(mappedAddress.getHouseNumber());
            addressEntity.setHouseNumberSuffix(mappedAddress.getHouseNumberSuffix());
            addressEntity.setPostalCode(mappedAddress.getPostalCode());
            addressEntity.setCity(mappedAddress.getCity());

            return posEntityMapper.fromEntity(posRepository.saveAndFlush(posEntity));
        } catch (DataIntegrityViolationException e) {
            // Translate database constraint violations to domain exceptions
            // This is the adapter's responsibility in hexagonal architecture
            if (e.getMessage() != null && e.getMessage().contains("pos_name_key")) {
                throw new DuplicatePosNameException(pos.getName());
            }
            // Re-throw if it's a different constraint violation
            throw e;
        }
    }
}
