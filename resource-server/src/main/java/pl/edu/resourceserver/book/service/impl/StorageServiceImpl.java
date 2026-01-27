package pl.edu.resourceserver.book.service.impl;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.resourceserver.book.service.S3Properties;
import pl.edu.resourceserver.book.service.contract.StorageService;
import pl.edu.resourceserver.exception.FolderNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
class StorageServiceImpl implements StorageService {
    private final S3Properties s3Properties;
    private final S3Template s3Template;

    public void uploadFiles(List<MultipartFile> files, String folderName) {
        for (MultipartFile file : files) {
            String key = assignFileName(folderName, file);
            try (InputStream inputStream = file.getInputStream()) {
                s3Template.upload(s3Properties.getBucket(), key, inputStream);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file to S3: " + file.getOriginalFilename());
            }
        }
    }

    public void deleteFolder(String folderName) {
        List<S3Resource> resources = s3Template.listObjects(s3Properties.getBucket(), folderName + "/");

        if (resources.isEmpty()) {
            throw new FolderNotFoundException("Folder with name - " + folderName + " not found");
        }

        for (S3Resource obj : resources) {
            s3Template.deleteObject(s3Properties.getBucket(), obj.getFilename());
        }
    }


    private String assignFileName(String folderName, MultipartFile file) {
        String contentType = file.getContentType();
        String key;

        if (contentType != null && contentType.startsWith("image/")) {
            key = folderName + s3Properties.getCoverKey();
        } else if ("application/pdf".equals(contentType)) {
            key = folderName + s3Properties.getBookKey();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        return key;
    }
}
