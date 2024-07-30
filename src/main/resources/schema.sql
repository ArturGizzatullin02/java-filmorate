create table IF NOT EXISTS GENRES
(
    GENRE_ID   BIGINT auto_increment,
    GENRE_NAME VARCHAR(50) UNIQUE not null,
    constraint GENRES_PK
        primary key (GENRE_ID)
);
create table IF NOT EXISTS RATINGS
(
    RATING_ID   BIGINT auto_increment,
    RATING_NAME CHARACTER VARYING(5) UNIQUE not null,
    constraint RATINGS_PK
        primary key (RATING_ID)
);
create table IF NOT EXISTS FILMS
(
    FILM_ID      BIGINT auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(200) not null,
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    BIGINT                 not null,
    constraint FILMS_PK
        primary key (FILM_ID),
    constraint FILMS_RATINGS_RATING_ID_FK
        foreign key (RATING_ID) references RATINGS ON DELETE RESTRICT
);
create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  BIGINT not null,
    GENRE_ID BIGINT not null,
    constraint FILM_GENRE_PK
        primary key (FILM_ID, GENRE_ID),
    constraint FILM_GENRE___FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint GENRE_FILM___FK
        foreign key (GENRE_ID) references GENRES ON DELETE RESTRICT
);
create table IF NOT EXISTS USERS
(
    USER_ID   BIGINT auto_increment,
    EMAIL     CHARACTER VARYING(50) not null,
    LOGIN     CHARACTER VARYING(50) not null,
    USER_NAME CHARACTER VARYING(50),
    BIRTHDAY  DATE                  not null,
    constraint USERS_PK
        primary key (USER_ID)
);
create table IF NOT EXISTS LIKES
(
    USER_ID BIGINT not null,
    FILM_ID BIGINT not null,
    constraint LIKES_PK
        primary key (USER_ID, FILM_ID),
    constraint LIKES_FILMS_FILM_ID_FK
        foreign key (FILM_ID) references FILMS ON DELETE CASCADE,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS ON DELETE CASCADE
);
create table IF NOT EXISTS FRIENDSHIPS
(
    USER_1_ID BIGINT not null,
    USER_2_ID BIGINT not null,
    constraint FRIENDSHIPS_PK
        primary key (USER_1_ID, USER_2_ID),
    constraint FRIENDSHIPS_USERS_USER_1_ID_FK
        foreign key (USER_1_ID) references USERS ON DELETE CASCADE,
    constraint FRIENDSHIPS_USERS_USER_2_ID_FK
        foreign key (USER_2_ID) references USERS ON DELETE CASCADE
);