package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmRepository {
    void add(Film film);

    void addLike(long filmId, long userId);

    Optional<Film> get(long id);

    Collection<Film> getAll();

    List<Film> getMostPopulars(int count);

    boolean filmExists(long id);

    void remove(long id);

    void removeLike(long filmId, long userId);

    void update(Film film);
}
