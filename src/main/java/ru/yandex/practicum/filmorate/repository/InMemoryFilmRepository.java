package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class InMemoryFilmRepository implements FilmRepository {
    Map<Long, Film> films = new HashMap<>();
    Map<Long, Set<Long>> likesByUserId = new HashMap<>();

    private long id = 1;

    private long nextId() {
        return id++;
    }

    @Override
    public void add(Film film) {
        film.setId(nextId());
        films.put(film.getId(), film);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Set<Long> likes = likesByUserId.computeIfAbsent(filmId, id -> new HashSet<>());
        likes.add(userId);
    }

    @Override
    public Optional<Film> get(long id) {
        return Optional.of(films.get(id));
    }

    @Override
    public Collection<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getMostPopulars(int count) {
        Map<Film, Long> filmLikeCounts = new HashMap<>();
        for (Map.Entry<Long, Set<Long>> likes : likesByUserId.entrySet()) {
            long countsOfLikes = likes.getValue().size();
            filmLikeCounts.put(films.get(likes.getKey()), countsOfLikes);
        }
        List<Map.Entry<Film, Long>> sortedFilmsByLikes = new ArrayList<>(filmLikeCounts.entrySet());
        sortedFilmsByLikes.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Film> topMovies = new ArrayList<>();
        for (int i = 0; i < Math.min(count, sortedFilmsByLikes.size()); i++) {
            topMovies.add(sortedFilmsByLikes.get(i).getKey());
        }
        return topMovies;
    }

    @Override
    public boolean filmExists(long id) {
        return films.containsKey(id);
    }

    @Override
    public void remove(long id) {
        films.remove(id);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        likesByUserId.get(filmId).remove(userId);
    }

    @Override
    public void update(Film film) {
        films.put(film.getId(), film);
    }
}
