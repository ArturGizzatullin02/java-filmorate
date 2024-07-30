package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.repository.rating.RatingRepository;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public Rating get(long id) {
        log.debug("Get rating with id {}", id);
        return ratingRepository.get(id).orElseThrow(() -> new NotFoundException("Рейтинг не найден", id));
    }

    public Collection<Rating> getAll() {
        log.debug("Get all ratings");
        return ratingRepository.getAll();
    }
}
