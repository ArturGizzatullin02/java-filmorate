package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{id}")
    public Rating get(@PathVariable long id) {
        return ratingService.get(id);
    }

    @GetMapping
    public Collection<Rating> getAll() {
        return ratingService.getAll();
    }
}
