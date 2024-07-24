package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Collection<Film> getAll() {
        log.info("GET /films");
        return filmRepository.getAll();
    }

    public Film get(long id) {
        log.info("GET /films/{id}");
        return filmRepository.get(id).orElseThrow(() -> new NotFoundException("Film not found"));
    }

    public Film add(Film film) {
        log.info("Add film {} - Started", film);
        filmRepository.add(film);
        log.info("Add film {} - Finished", film);
        return film;
    }

    public void addLike(long id, long userId) {
        log.info("PUT /films/{id}/like/{userId}");
        get(id);
        userService.get(userId);
        filmRepository.addLike(id, userId);
    }

    public Film update(Film film) {
        log.info("Update film {} - Started", film);
        if (filmRepository.filmExists(film.getId())) {
            filmRepository.update(film);
            log.info("Update film {} - Finished", film);
        } else {
            log.info("PUT /films ==> Film not found");
            throw new NotFoundException("Фильм с таким ID не найден");
        }
        return film;
    }

    public List<Film> getMostPopular(int count) {
        log.info("GET /films/popular");
        return filmRepository.getMostPopulars(count);
    }

    public void removeLike(long id, long userId) {
        log.info("DELETE /films/{id}/like/{userId}");
        get(id);
        userService.get(userId);
        filmRepository.removeLike(id, userId);
    }
}
