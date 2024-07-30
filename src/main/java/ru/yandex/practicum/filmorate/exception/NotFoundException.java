package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final long id;

    public NotFoundException(String message, long id) {
        super(message);
        this.id = id;
    }

}
