package ru.yandex.practicum.filmorate.repository.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RatingRepository {
    Optional<Rating> get(long id);

    Collection<Rating> getAll();

    boolean ratingExists(long id);

    boolean ratingExists(List<Long> ids);
}
