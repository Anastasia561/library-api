package pl.edu.resourceserver;

import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.edu.resourceserver.book.service.S3Properties;
import pl.edu.resourceserver.config.MySQLTestContainerConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Import(MySQLTestContainerConfig.class)
public abstract class AbstractIntegrationTest {
    protected static final String FOLDER = "test-folder";

    @Autowired
    protected S3Template s3Template;

    @Autowired
    protected S3Properties s3Properties;

    @Value("${app.aws.s3.bucket}")
    protected String bucketName;

    @Container
    private static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:3.0"))
                    .withServices(LocalStackContainer.Service.S3)
                    .withReuse(true);

    @DynamicPropertySource
    private static void registerAwsProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.credentials.access-key", localStack::getAccessKey);
        registry.add("spring.cloud.aws.credentials.secret-key", localStack::getSecretKey);
        registry.add("spring.cloud.aws.s3.endpoint",
                () -> localStack.getEndpointOverride(LocalStackContainer.Service.S3).toString());
    }

    @BeforeAll
    protected static void init() throws InterruptedException, IOException {
        localStack.execInContainer("awslocal", "s3", "mb", "s3://test-bucket");
    }

    @BeforeEach
    protected void setUp() {
        byte[] content = "Test content".getBytes();
        String fullKey = FOLDER + s3Properties.getBookKey();
        String coverKey = FOLDER + s3Properties.getCoverKey();
        String previewKey = FOLDER + s3Properties.getPreviewKey();

        s3Template.upload(bucketName, fullKey, new ByteArrayInputStream(content));
        s3Template.upload(bucketName, coverKey, new ByteArrayInputStream(content));
        s3Template.upload(bucketName, previewKey, new ByteArrayInputStream(content));
    }

    @AfterEach
    protected void cleanup() {
        s3Template.listObjects(bucketName, "")
                .forEach(obj -> s3Template.deleteObject(bucketName, obj.getFilename()));
    }
}
