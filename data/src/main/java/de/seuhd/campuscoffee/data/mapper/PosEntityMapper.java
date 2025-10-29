package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.AddressEntity;
import de.seuhd.campuscoffee.data.persistence.PosEntity;
import de.seuhd.campuscoffee.domain.model.Pos;
import org.mapstruct.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface PosEntityMapper {
    @Mapping(source = "address.street", target = "street")
    @Mapping(source = "address.postalCode", target = "postalCode")
    @Mapping(source = "address.city", target = "city")
    @Mapping(target = "houseNumber", expression = "java(mergeHouseNumberAndSuffix(source))")
    Pos fromEntity(PosEntity source);

    @Mapping(target = "address", expression = "java(createAddressEntity(source))")
    PosEntity toEntity(Pos source);

    @SuppressWarnings("unused")
    default String mergeHouseNumberAndSuffix(PosEntity entity) {
        if (entity.getAddress() == null || entity.getAddress().getHouseNumber() == null) {
            return null;
        }

        String houseNumberWithSuffix = entity.getAddress().getHouseNumber().toString();
        if (entity.getAddress().getHouseNumberSuffix() != null) {
            houseNumberWithSuffix += entity.getAddress().getHouseNumberSuffix();
        }
        return houseNumberWithSuffix;
    }

    @SuppressWarnings("unused")
    default AddressEntity createAddressEntity(Pos source) {
        if (source == null) {
            return null;
        }

        AddressEntity address = new AddressEntity();
        address.setStreet(source.getStreet());
        address.setCity(source.getCity());
        address.setPostalCode(source.getPostalCode());

        // Parse house number and suffix
        if (!source.getHouseNumber().isEmpty()) {
            String houseNumber = source.getHouseNumber();
            String numericPart = houseNumber.replaceAll("[^0-9]", "");
            String suffixPart = houseNumber.replaceAll("[0-9]", "");

            if (!numericPart.isEmpty()) {
                address.setHouseNumber(Integer.parseInt(numericPart));
            }

            if (!suffixPart.isEmpty()) {
                address.setHouseNumberSuffix(suffixPart.charAt(0));
            }
        }

        return address;
    }
}
