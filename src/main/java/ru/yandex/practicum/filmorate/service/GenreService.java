package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {
    private final GenreRepository genreRepository;

    public Genre get(long id) {
        log.debug("Get genre by id: {}", id);
        return genreRepository.get(id).orElseThrow(() -> new NotFoundException("Жанр не найден"));
    }

    public Collection<Genre> getAll() {
        log.debug("Get all genres");
        return genreRepository.getAll();
    }
}
