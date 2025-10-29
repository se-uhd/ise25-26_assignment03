package de.seuhd.campuscoffee.api.dtos;

import de.seuhd.campuscoffee.domain.model.CampusType;
import de.seuhd.campuscoffee.domain.model.PosType;
import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * DTO for POS metadata.
 *
 */
@Data
@Builder(toBuilder = true)
public class PosDto {
        private Long id; // id is null when creating a new task
        private LocalDateTime createdAt; // is null when using DTO to create a new POS
        private LocalDateTime updatedAt; // is set when creating or updating a POS
        @NonNull
        private final String name;
        @NonNull
        private final String description;
        @NonNull
        private final PosType type;
        @NonNull
        private final CampusType campus;
        @NonNull
        private final String street;
        @NonNull
        private final String houseNumber;
        @NonNull
        private final Integer postalCode;
        @NonNull
        private final String city;
}
