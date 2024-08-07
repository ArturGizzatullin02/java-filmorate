package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Marker;

import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @Validated(Marker.OnGet.class)
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsList(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    public User add(@RequestBody @Valid User user) {
        return userService.add(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @PutMapping
    @Validated(Marker.OnUpdate.class)
    public User update(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
    }
}
