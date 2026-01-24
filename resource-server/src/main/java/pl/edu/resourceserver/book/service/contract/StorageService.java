package pl.edu.resourceserver.book.service.contract;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void uploadFiles(List<MultipartFile> files, String folderName);

    void deleteFolder(String folderName);
}
