package de.seuhd.campuscoffee;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import io.restassured.http.ContentType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Utility class for system tests.
 * Provides methods to manage PostgreSQL testcontainers and perform common API operations.
 */
public class TestUtils {
    @SuppressWarnings("resource")
    public static PostgreSQLContainer<?> getPostgresContainer() {
        return new PostgreSQLContainer<>(
                DockerImageName.parse("postgres:17-alpine"))
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("postgres")
                .withReuse(true);
    }

    public static void configurePostgresContainers (DynamicPropertyRegistry registry, PostgreSQLContainer<?> postgresContainer) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    public static List<PosDto> retrievePos() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/pos")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("$", PosDto.class)
                .stream()
                .toList();
    }

    public static PosDto retrievePosById(Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/pos/{id}", id)
                .then()
                .statusCode(200)
                .extract().as(PosDto.class);
    }

    public static List<PosDto> createPos(List<PosDto> posList) {
        return posList.stream()
                .map(posDto -> given()
                        .contentType(ContentType.JSON)
                        .body(posDto)
                        .when()
                        .post("/api/pos")
                        .then()
                        .statusCode(201)
                        .extract().as(PosDto.class)
                )
                .toList();
    }

    public static List<PosDto> updatePos(List<PosDto> posList) {
        return posList.stream()
                .map(posDto -> given()
                        .contentType(ContentType.JSON)
                        .body(posDto)
                        .when()
                        .put("/api/pos/{id}", posDto.getId())
                        .then()
                        .statusCode(200)
                        .extract().as(PosDto.class)
                )
                .toList();
    }
}
