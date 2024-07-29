package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.rating.RatingRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final RatingRepository ratingRepository;

    public Collection<Film> getAll() {
        log.info("GET /films");
        return filmRepository.getAll();
    }

    public Film get(long id) {
        log.info("GET /films/{id}");
        return filmRepository.get(id).orElseThrow(() -> new NotFoundException("Фильм с данным ID не найден"));
    }

    public Film add(Film film) {
        log.info("Add film {} - Started", film);
        ratingExists(film);
        genreExists(film);
        log.info("Add film {} - Finished", film);
        return filmRepository.add(film);
    }

    public void addLike(long id, long userId) {
        log.info("PUT /films/{id}/like/{userId}");
        get(id);
        userRepository.get(userId).orElseThrow(() -> new NotFoundException("Пользователь с данным ID не найден"));
        filmRepository.addLike(id, userId);
    }

    public Film update(Film film) {
        log.info("Update film {} - Started", film);
        if (filmRepository.get(film.getId()).isPresent()) {
            ratingExists(film);
            genreExists(film);
            Film updatedFilm = filmRepository.update(film);
            log.info("Film is updated: {}", updatedFilm);
            return updatedFilm;
        }
        throw new NotFoundException("Фильм не найден");
    }

    public List<Film> getMostPopular(int count) {
        log.info("GET /films/popular");
        return filmRepository.getMostPopulars(count);
    }

    public void removeLike(long id, long userId) {
        log.info("DELETE /films/{id}/like/{userId}");
        get(id);
        userRepository.get(userId).orElseThrow(() -> new NotFoundException("Пользователь с данным ID не найден"));
        filmRepository.removeLike(id, userId);
    }

    private void ratingExists(Film film) {
        if (film.getMpa() != null && !ratingRepository.ratingExists(film.getMpa().getId())) {
            throw new ValidationException("Рейтинг не найден. Фильм не может быть обновлен");
        }
    }

    private void genreExists(Film film) {
        if (!genreRepository.genreExists(
                film.getGenres().stream()
                        .map(Genre::getId)
                        .collect(Collectors.toList()))
        ) {
            throw new ValidationException("Жанр не найден. Фильм не может быть обновлен");
        }
    }
}
