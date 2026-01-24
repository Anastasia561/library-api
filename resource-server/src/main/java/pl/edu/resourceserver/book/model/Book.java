package pl.edu.resourceserver.book.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import pl.edu.resourceserver.author.model.Author;
import pl.edu.resourceserver.genre.model.Genre;
import pl.edu.resourceserver.publisher.model.Publisher;

@Getter
@Setter
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(name = "year", nullable = false)
    private int publicationYear;
    private Integer pages;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
}
