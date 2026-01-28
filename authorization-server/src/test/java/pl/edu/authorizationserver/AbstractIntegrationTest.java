package pl.edu.authorizationserver;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.edu.authorizationserver.config.TestContainerConfig;

@ActiveProfiles("test")
@Import(TestContainerConfig.class)
public class AbstractIntegrationTest {
}
