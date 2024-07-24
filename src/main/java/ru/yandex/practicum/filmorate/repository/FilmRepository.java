package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmRepository {
    public void add(Film film);

    public void addLike(long filmId, long userId);

    public Optional<Film> get(long id);

    public Collection<Film> getAll();

    public List<Film> getMostPopulars(int count);

    boolean filmExists(long id);

    public void remove(long id);

    public void removeLike(long filmId, long userId);

    public void update(Film film);
}
