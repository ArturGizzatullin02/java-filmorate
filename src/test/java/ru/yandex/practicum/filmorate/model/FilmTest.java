package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.usingContext().getValidator();
    }

    @Test
    void shouldBeFalseIfNameIsNull() {
        Film film = new Film();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Имя не может быть пустым");
    }

    @Test
    void shouldBeFalseIfNameIsBlank() {
        Film film = new Film();
        film.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Имя не может содержать только пробелы");
    }

    @Test
    void shouldBeTrueIfNameIsNotEmpty() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        System.out.println(film);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Если имя не пустое, то ошибки не должно быть");
    }

    @Test
    void shouldBeFalseIfDescriptionIsBiggerThan200() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        film.setDescription("Исследования показывают, что занятия йогой способствуют снижению уровня стресса" +
                ", улучшению гибкости и повышению телесной осознанности. Практика йоги также связана " +
                "с улучшением физического здоровья, включая укрепление мышц и улучшение осанки. Кроме того" +
                ", медитация, часто входящая в программы йоги, может помочь улучшить фокусировку внимания и" +
                " снизить уровень тревоги. Однако перед началом занятий йогой важно проконсультироваться с врачом" +
                ", особенно при наличии каких-либо медицинских проблем или ограничений.");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Описание не может быть больше 200 символов");
    }

    @Test
    void shouldBeTrueIfDescriptionIsSmallerThan200() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        film.setDescription("Описание фильма");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Если описание меньше 200 символов, то ошибки не должно быть");
    }

    @Test
    void shouldBeFalseIfReleaseDateIsisAfterThan_1895_1_1() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Дата выхода фильма должна быть не раньше 1895 года");
    }

    @Test
    void shouldBeTrueIfReleaseDateIsisAfterThan_1895_1_1() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        film.setReleaseDate(LocalDate.of(2003, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Если дата выхода фильма позже 1895 года, то ошибки не должно быть");
    }

    @Test
    void shouldBeFalseIfDurationIsNegative() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(-90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Длительность фильма не может быть отрицательной");
    }

    @Test
    void shouldBeTrueIfDurationIsNegative() {
        Film film = new Film();
        film.setName("Test");
        film.setDuration(90);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty(), "Если длительность фильма положительная, то ошибки не должно быть");
    }
}
