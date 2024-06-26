package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.Marker;
import ru.yandex.practicum.filmorate.validator.ReleaseDateConstraint;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull(groups = {Marker.OnUpdate.class, Marker.OnGet.class})
    private long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Название фильма должно быть не более 200 символов")
    private String description;
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private int duration;
}
