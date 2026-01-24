package pl.edu.resourceserver.book.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.book.service.contract.UrlGeneratorService;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@RequiredArgsConstructor
@Service
class UrlGeneratorServiceImpl implements UrlGeneratorService {
    private final S3Presigner s3Presigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Override
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

    @Override
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

    @Override
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
