package de.seuhd.campuscoffee.domain.tests;

import de.seuhd.campuscoffee.domain.model.*;
import de.seuhd.campuscoffee.domain.ports.PosService;
import org.apache.commons.lang3.SerializationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test fixtures for POS entities.
 */
public class TestFixtures {
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2025, 10, 29, 12, 0, 0);

    private static final List<Pos> POS_LIST = List.of(
            new Pos(1L, DATE_TIME, DATE_TIME, "Schmelzpunkt", "Great waffles", PosType.CAFE, CampusType.ALTSTADT, "Hauptstraße", "90", 69117, "Heidelberg"),
            new Pos(1L, DATE_TIME, DATE_TIME, "Bäcker Görtz ", "Walking distance to lecture hall", PosType.BAKERY, CampusType.INF, "Berliner Str.", "43", 69120, "Heidelberg"),
            new Pos(1L, DATE_TIME, DATE_TIME, "Café Botanik", "Outdoor seating available", PosType.CAFETERIA, CampusType.INF, "Im Neuenheimer Feld", "304", 69120, "Heidelberg"),
            new Pos(1L, DATE_TIME, DATE_TIME, "New Vending Machine", "Use only in case of emergencies", PosType.VENDING_MACHINE, CampusType.BERGHEIM, "Teststraße", "99a", 12345, "Other City")
    );

    public static List<Pos> getPosList() {
        return POS_LIST.stream()
                .map(SerializationUtils::clone) // prevent issues when tests modify the fixture objects
                .toList();
    }

    public static List<Pos> getPosFixturesForInsertion() {
        return getPosList().stream()
                .map(user -> user.toBuilder().id(null).createdAt(null).updatedAt(null).build())
                .toList();
    }

    public static List<Pos> createPosFixtures(PosService posService) {
        return getPosFixturesForInsertion().stream()
                .map(posService::upsert)
                .collect(Collectors.toList());
    }
}
