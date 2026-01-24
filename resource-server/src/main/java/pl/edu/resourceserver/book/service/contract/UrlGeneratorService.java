package pl.edu.resourceserver.book.service.contract;

import java.net.URL;

public interface UrlGeneratorService {
    URL generateBookCoverURL(String folderName);

    URL generateBookDownloadURL(String folderName, String bookName, boolean isFull);

    URL generateBookURL(String folderName, boolean isFull);
}
