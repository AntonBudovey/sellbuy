package ee.taltech.iti03022024backend;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER;

    static {
        POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:latest");
        POSTGRES_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);

    }
}
