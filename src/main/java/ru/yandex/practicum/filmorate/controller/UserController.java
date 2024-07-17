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
import java.util.List;
import java.util.Set;

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

    @GetMapping("{id}")
    public User get(@PathVariable long id) {
        return userService.get(id);
    }

    @GetMapping("{id}/friends")
    public Set<Long> getFriendsList(@PathVariable long id) {
        return userService.getFriendsIds(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Set<Long> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
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
    public Long deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        return userService.removeFriend(id, friendId);
    }

}
