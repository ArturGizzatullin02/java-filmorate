package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping
    @Validated(Marker.OnGet.class)
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public User add(@RequestBody @Valid User user) {
        return userService.add(user);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }
}
