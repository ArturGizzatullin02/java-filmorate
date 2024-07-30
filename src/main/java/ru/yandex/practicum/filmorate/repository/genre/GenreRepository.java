package ru.yandex.practicum.filmorate.repository.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> get(long id);

    Collection<Genre> getAll();

    boolean genreExists(long id);

    boolean genreExists(List<Long> ids);
}
