INSERT INTO author (first_name, last_name, pen_name)
VALUES ('Joanne', 'Rowling', NULL),
       ('Stephen', 'King', NULL),
       ('Eric', 'Blair', 'George Orwell');

INSERT INTO genre (name)
VALUES ('Fantasy'),
       ('Horror'),
       ('Dystopian');

INSERT INTO publisher (name)
VALUES ('Bloomsbury Publishing'),
       ('Scribner'),
       ('Secker & Warburg');

INSERT INTO book (title, isbn, year, pages, author_id, publisher_id, genre_id)
VALUES ("Harry Potter and the Philosopher\'s Stone", '9780747532699', 1997, 223, 1, 1, 1),

       ('The Shining', '9780385121675', 1977, NULL, 2, 2, 2),

       ('1984', '9780306406164', 1949, 328, 3, 3, 3);
