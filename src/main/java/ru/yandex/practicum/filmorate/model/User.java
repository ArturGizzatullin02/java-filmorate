package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.time.LocalDate;

@Data
public class User {
    @NotNull(groups = {Marker.OnUpdate.class, Marker.OnGet.class}, message = "ID не может быть null")
    private Long id;
    @Email(message = "Электронная почта должна содержать символ @")
    @NotBlank(message = "Электронная почта не может быть пустой или состоять только из пробелов")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения должна быть либо в прошлом, либо сегодня")
    @NotNull(message = "Дата рождения не может быть null")
    private LocalDate birthday;
}
