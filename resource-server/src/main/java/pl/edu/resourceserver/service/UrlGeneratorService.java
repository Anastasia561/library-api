package pl.edu.resourceserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@Service
public class UrlGeneratorService {
    S3Presigner s3Presigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    public UrlGeneratorService(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public URL generateBookCoverURL(String folderName) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName + "/cover")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    public URL generateBookDownloadURL(String folderName, String bookName, boolean isFull) {
        String suffix = isFull ? "_full.pdf\"" : "_preview.pdf\"";
        String keySuffix = isFull ? "/book.pdf" : "/preview.pdf";
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .responseContentDisposition("attachment; filename=\"" + sanitize(bookName) + suffix)
                .key(folderName + keySuffix)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    public URL generateBookURL(String folderName, boolean isFull) {
        String keySuffix = isFull ? "/book.pdf" : "/preview.pdf";
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(folderName + keySuffix)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(5))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    private String sanitize(String title) {
        return title.replaceAll("[^a-zA-Z0-9\\s.-]", "").replaceAll("\\s+", "_");
    }
}
