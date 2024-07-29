MERGE INTO GENRES (GENRE_ID, GENRE_NAME)
    VALUES   (1, 'Комедия'),
             (2, 'Драма'),
             (3, 'Мультфильм'),
             (4, 'Триллер'),
             (5, 'Документальный'),
             (6, 'Боевик');

MERGE INTO RATINGS (RATING_ID, RATING_NAME)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

INSERT INTO USERS
(EMAIL, LOGIN, USER_NAME, BIRTHDAY)
VALUES
    ('vladimirov@gmail.com', 'Vladimir', 'Vladimirov', '1998-03-17'),
    ('ivanov@gmail.com', 'Ivanov', 'Ivan', '2005-07-27');

INSERT INTO FILMS
(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
VALUES
    ('troya', 'brave man', '1998-06-17', 90, 5),
    ('vampire', 'sumerki', '2017-02-08', 100, 2),
    ('alley', 'autumn film', '2005-09-10', 120, 2);