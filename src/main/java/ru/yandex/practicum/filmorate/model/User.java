package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.sql.Date;
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

    public MapSqlParameterSource toMap() {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_ID", id);
        params.addValue("USER_NAME", name);
        params.addValue("EMAIL", email);
        params.addValue("LOGIN", login);
        params.addValue("NAME", name);
        params.addValue("BIRTHDAY", Date.valueOf(birthday));
        return params;
    }
}
