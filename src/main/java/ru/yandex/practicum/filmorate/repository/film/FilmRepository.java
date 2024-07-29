package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmRepository {
    Film add(Film film);

    void addLike(long filmId, long userId);

    Optional<Film> get(long id);

    Collection<Film> getAll();

    List<Film> getMostPopulars(int count);

    boolean filmExists(long id);

    void remove(long id);

    void removeLike(long filmId, long userId);

    Film update(Film film);
}
