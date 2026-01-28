package pl.edu.resourceserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@RequiredArgsConstructor
public class AWSConfiguration {

    private final AwsRegionProvider regionProvider;
    private final AwsCredentialsProvider awsCredentialsProvider;

    @Bean
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(regionProvider.getRegion())
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}