package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmRepository {
    Map<Long, Film> films = new HashMap<>();
    Map<Long, Set<Long>> likesByUserId = new HashMap<>();

    public void add(Film film);

    public void addLike(long filmId, long userId);

    public Film get(long id);

    public Collection<Film> getAll();

    public List<Film> getMostPopulars(int count);

    public Map<Long, Film> getMap();

    public void remove(long id);

    public void removeLike(long filmId, long userId);

    public void update(Film film);
}
