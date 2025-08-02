package pl.edu.resourceserver.dto;

import java.net.URL;

public class BookLibrarianResponseDto {
    private String isbn;
    private String title;
    private int publicationYear;
    private Integer pages;
    private String author;
    private String authorPenName;
    private String publisher;
    private String genre;
    private URL coverImage;
    private URL fullBookDocument;
    private URL previewBookDocument;

    public URL getFullBookDocument() {
        return fullBookDocument;
    }

    public void setFullBookDocument(URL fullBookDocument) {
        this.fullBookDocument = fullBookDocument;
    }

    public URL getPreviewBookDocument() {
        return previewBookDocument;
    }

    public void setPreviewBookDocument(URL previewBookDocument) {
        this.previewBookDocument = previewBookDocument;
    }

    public URL getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(URL coverImage) {
        this.coverImage = coverImage;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorPenName() {
        return authorPenName;
    }

    public void setAuthorPenName(String authorPenName) {
        this.authorPenName = authorPenName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
