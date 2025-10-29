package de.seuhd.campuscoffee.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain class that stores the POS (Point of Sale) metadata.
 */
@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Pos implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L; // required to clone objects (see TestFixtures class).

        private Long id; // null when the POS has not been created yet
        private LocalDateTime createdAt; // set on POS creation
        private LocalDateTime updatedAt; // set on POS creation and update

        @NonNull
        private String name;

        @NonNull
        private String description;

        @NonNull
        private PosType type;

        @NonNull
        private CampusType campus;

        @NonNull
        private String street;

        @NonNull
        private String houseNumber;

        @NonNull
        private Integer postalCode;

        @NonNull
        private String city;
}
