INSERT INTO author (first_name, last_name, pen_name)
VALUES ('Joanne', 'Rowling', NULL);

INSERT INTO genre (name)
VALUES ('Fantasy');

INSERT INTO publisher (name)
VALUES ('Bloomsbury Publishing');

INSERT INTO book (title, isbn, year, pages, author_id, publisher_id, genre_id)
VALUES ('Test', '9780747532699', 1997, 223, 1, 1, 1);
