package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.Pos;
import de.seuhd.campuscoffee.domain.exceptions.PosNotFoundException;
import de.seuhd.campuscoffee.domain.ports.PosDataService;
import de.seuhd.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the POS service that handles business logic related to POS entities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PosServiceImpl implements PosService {
    private final PosDataService posDataService;

    @Override
    public void clear() {
        log.warn("Clearing all POS data");
        posDataService.clear();
    }

    @Override
    @NonNull
    public List<Pos> getAll() {
        log.debug("Retrieving all POS");
        return posDataService.getAll();
    }

    @Override
    @NonNull
    public Pos getById(@NonNull Long id) throws PosNotFoundException {
        log.debug("Retrieving POS with ID: {}", id);
        return posDataService.getById(id);
    }

    @Override
    @NonNull
    public Pos upsert(@NonNull Pos pos) throws PosNotFoundException {
        if (pos.getId() == null) {
            // Create new POS
            return createPos(pos);
        } else {
            // Update existing POS
            return updatePos(pos);
        }
    }

    /**
     * Creates a new POS. Handles business rules for creation.
     * Note: Name uniqueness is enforced by database constraint (translated by data layer)
     * Note: Timestamps are set by JPA lifecycle callbacks in the entity
     */
    private Pos createPos(Pos pos) {
        log.info("Creating new POS: {}", pos.getName());

        // Database constraint enforces name uniqueness - data layer will throw DuplicatePosNameException if violated
        // JPA @PrePersist callback sets timestamps automatically
        Pos createdPos = posDataService.upsert(pos);
        log.info("Successfully created POS with ID: {}", createdPos.getId());

        return createdPos;
    }

    /**
     * Updates an existing POS. Handles business rules for update (validates that POS exists).
     * Note: Name uniqueness is enforced by database constraint (translated by data layer)
     * Note: Update timestamp is set by JPA lifecycle callback in the entity
     */
    private Pos updatePos(Pos pos) throws PosNotFoundException {
        log.info("Updating POS with ID: {}", pos.getId());

        // POS must exist before update
        posDataService.getById(pos.getId());

        // Database constraint enforces name uniqueness - data layer will throw DuplicatePosNameException if violated
        // JPA @PreUpdate callback sets updatedAt timestamp automatically
        Pos updatedPos = posDataService.upsert(pos);
        log.info("Successfully updated POS with ID: {}", updatedPos.getId());

        return updatedPos;
    }
}
