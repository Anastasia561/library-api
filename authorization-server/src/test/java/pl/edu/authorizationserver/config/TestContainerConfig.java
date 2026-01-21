package pl.edu.authorizationserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
@RequiredArgsConstructor
public class TestContainerConfig {
    private final TestContainerProperties props;

    @Value("${testcontainers.reuse.enable}")
    private boolean reusable;

    @Bean
    @ServiceConnection
    MySQLContainer<?> mySQLContainer() {
        return new MySQLContainer<>(props.getImage())
                .withUsername(props.getUsername())
                .withPassword(props.getPassword())
                .withDatabaseName(props.getDatabase())
                .withReuse(reusable);
    }
}