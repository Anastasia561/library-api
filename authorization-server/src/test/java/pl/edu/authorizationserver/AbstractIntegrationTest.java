package pl.edu.authorizationserver;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.authorizationserver.config.TestContainerConfig;

@ActiveProfiles("test")
@Testcontainers
@Import(TestContainerConfig.class)
public class AbstractIntegrationTest {
}
