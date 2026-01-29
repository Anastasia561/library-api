-- Table: author
CREATE TABLE author
(
    id         bigint      NOT NULL AUTO_INCREMENT,
    first_name varchar(30) NOT NULL,
    last_name  varchar(30) NOT NULL,
    pen_name   varchar(30) NULL,
    CONSTRAINT author_pk PRIMARY KEY (id)
);

-- Table: book
CREATE TABLE book
(
    id           bigint      NOT NULL AUTO_INCREMENT,
    title        varchar(50) NOT NULL,
    isbn         varchar(13) NOT NULL UNIQUE,
    year         int         NOT NULL,
    pages        int NULL,
    author_id    bigint      NOT NULL,
    publisher_id bigint      NOT NULL,
    genre_id     bigint      NOT NULL,
    CONSTRAINT book_pk PRIMARY KEY (id)
);

-- Table: genre
CREATE TABLE genre
(
    id   bigint      NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    CONSTRAINT genre_pk PRIMARY KEY (id)
);

-- Table: publisher
CREATE TABLE publisher
(
    id   bigint      NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    CONSTRAINT publisher_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: book_author (table: book)
ALTER TABLE book
    ADD CONSTRAINT book_author FOREIGN KEY book_author (author_id)
    REFERENCES author (id);

-- Reference: book_genre (table: book)
ALTER TABLE book
    ADD CONSTRAINT book_genre FOREIGN KEY book_genre (genre_id)
    REFERENCES genre (id);

-- Reference: book_publisher (table: book)
ALTER TABLE book
    ADD CONSTRAINT book_publisher FOREIGN KEY book_publisher (publisher_id)
    REFERENCES publisher (id);
