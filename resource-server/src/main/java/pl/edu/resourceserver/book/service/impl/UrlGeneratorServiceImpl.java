package pl.edu.resourceserver.book.service.impl;

import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.resourceserver.book.service.S3Properties;
import pl.edu.resourceserver.book.service.contract.UrlGeneratorService;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@RequiredArgsConstructor
@Service
class UrlGeneratorServiceImpl implements UrlGeneratorService {
    private final S3Template s3Template;
    private final S3Properties s3Properties;
    private final S3Presigner s3Presigner;

    @Override
    public URL generateBookCoverURL(String folderName) {
        String key = folderName + s3Properties.getCoverKey();
        return s3Template.createSignedGetURL(s3Properties.getBucket(), key,
                Duration.ofMinutes(s3Properties.getUrlDurationMin()));
    }

    @Override
    public URL generateBookURL(String folderName, boolean isFull) {
        String keySuffix = isFull ? s3Properties.getBookKey() : s3Properties.getPreviewKey();
        String key = folderName + keySuffix;
        return s3Template.createSignedGetURL(s3Properties.getBucket(), key,
                Duration.ofMinutes(s3Properties.getUrlDurationMin()));
    }

    @Override
    public URL generateBookDownloadURL(String folderName, String bookName, boolean isFull) {
        String suffix = isFull ? "_full.pdf\"" : "_preview.pdf\"";
        String keySuffix = isFull ? s3Properties.getBookKey() : s3Properties.getPreviewKey();
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .responseContentDisposition("attachment; filename=\"" + sanitize(bookName) + suffix)
                .key(folderName + keySuffix)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(request)
                .signatureDuration(Duration.ofMinutes(s3Properties.getUrlDurationMin()))
                .build();

        return s3Presigner.presignGetObject(presignRequest).url();
    }

    private String sanitize(String title) {
        return title.replaceAll("[^a-zA-Z0-9\\s.-]", "").replaceAll("\\s+", "_");
    }
}
