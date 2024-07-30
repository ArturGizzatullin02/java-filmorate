package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validator.Marker;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Genre {
    @NotNull(groups = {Marker.OnUpdate.class, Marker.OnGet.class})
    private long id;
    @NotBlank(message = "Название жанра не может быть пустым")
    private String name;
}
