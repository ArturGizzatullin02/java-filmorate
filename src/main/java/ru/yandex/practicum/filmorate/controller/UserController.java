package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Long, User> users = new HashMap<>();
    long id = 1;

    @GetMapping
    @Validated(Marker.OnGet.class)
    public Collection<User> getUsers() {
        log.info("GET /users");
        return users.values();
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public User addUser(@RequestBody @Valid User user) {
        log.info("POST /users ==> Started");
        long newId = nextId();
        user.setId(newId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(newId, user);
        log.info("POST /users ==> Finished");
        return user;
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User updateUser(@RequestBody @Valid User user) {
        log.info("PUT /users ==> Started");
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("PUT /users ==> Finished");
        } else {
            log.info("PUT /users ==> User not found");
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        return user;
    }

    private long nextId() {
        return id++;
    }
}
