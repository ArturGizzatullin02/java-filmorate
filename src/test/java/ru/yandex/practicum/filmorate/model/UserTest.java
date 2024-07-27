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

public class UserTest {
    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.usingContext().getValidator();
    }

    @Test
    void shouldBeFalseIfIdIsNull() {

    }

    @Test
    void shouldBeFalseIfEmailIsNotContainsEmailSymbol() {
        User user = new User();
        user.setEmail("emailTest");
        user.setLogin("test");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Почта должна содержать @");
    }

    @Test
    void shouldBeFalseIfEmailIsNull() {
        User user = new User();
        user.setLogin("test");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Почта не может быть пустой");
    }

    @Test
    void shouldBeFalseIfEmailIsBlank() {
        User user = new User();
        user.setEmail(" ");
        user.setLogin("test");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Почта не может состоять только из пробелов");
    }

    @Test
    void shouldBeTrueIfEmailIsNotBlankAndContainsEmailSymbol() {
        User user = new User();
        user.setEmail("emailTest@gmail.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.now());
        user.setName("name");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Если почта не пустая, не состоит только из пробелов и содержит @" +
                ", то ошибки быть не должно");
    }

    @Test
    void shouldBeFalseIfLoginIsNull() {
        User user = new User();
        user.setEmail("emailTest@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Логин не может быть пустой");
    }

    @Test
    void shouldBeFalseIfLoginIsBlank() {
        User user = new User();
        user.setLogin(" ");
        user.setEmail("emailTest@gmail.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Логин не может состоять только из пробелов");
    }

    @Test
    void shouldBeTrueIfLoginIsNotBlank() {
        User user = new User();
        user.setLogin("test");
        user.setEmail("emailTest@gmail.com");
        user.setBirthday(LocalDate.now());
        user.setName("name");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Если логин не пустой и не состоит только из пробелов" +
                ", то ошибки быть не должно");
    }

    @Test
    void shouldBeFalseIfBirthdayIsFuture() {
        User user = new User();
        user.setLogin("test");
        user.setEmail("emailTest@gmail.com");
        user.setBirthday(LocalDate.of(2030, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "День рождения не может быть в будущем");
    }

    @Test
    void shouldBeTrueIfBirthdayIsFuture() {
        User user = new User();
        user.setLogin("test");
        user.setEmail("emailTest@gmail.com");
        user.setBirthday(LocalDate.of(2003, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Если день рождения в прошлом, то ошибки быть не должно");
    }
}
