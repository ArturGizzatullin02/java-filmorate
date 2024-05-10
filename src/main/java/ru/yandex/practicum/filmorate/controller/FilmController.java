package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    Map<Long, Film> films = new HashMap<>();
    long id = 1;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("GET /films");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film {} - Started", film);
        film.setId(nextId());
        films.put(film.getId(), film);
        log.info("Add film {} - Finished", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film {} - Started", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Update film {} - Finished", film);
        } else {
            log.info("PUT /films ==> Film not found");
            throw new NotFoundException("Фильм с таким ID не найден");
        }
        return film;
    }

    private long nextId() {
        return id++;
    }
}
