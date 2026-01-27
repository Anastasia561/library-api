package pl.edu.resourceserver.book.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.aws.s3")
@Getter
@Setter
public class S3Properties {
    private String bucket;
    private String bookKey;
    private String coverKey;
    private String previewKey;
    private Integer urlDurationMin;
}
