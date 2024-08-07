package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Collection<User> getAll() {
        log.info("GET /users");
        return userRepository.getAll();
    }

    public User get(long id) {
        log.info("GET /users");
        return userRepository.get(id).orElseThrow(() -> new NotFoundException("Пользователь с данным ID не найден", id));
    }

    public List<User> getFriends(long id) {
        log.info("GET /users{id}");
        get(id);
        return userRepository.getFriends(id);
    }

    public User add(User user) {
        log.info("POST /users ==> Started");
        validateUserName(user);
        userRepository.add(user);
        log.info("POST /users ==> Finished");
        return user;
    }

    public User update(User user) {
        log.info("PUT /users ==> Started");
        validateUserName(user);
        if (userRepository.get(user.getId()).isPresent()) {
            userRepository.update(user);
            log.info("PUT /users ==> Finished");
        } else {
            log.info("PUT /users ==> User not found");
            throw new NotFoundException("Пользователь с таким ID не найден", user.getId());
        }
        return user;
    }

    public void addFriend(long userId, long friendId) {
        log.info("PUT /users/{id}/friends/{friendId}");
        get(friendId);
        get(userId);
        userRepository.addFriend(userId, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        log.info("DELETE /users/{id}/friends/{friendId}");
        get(friendId);
        get(userId);
        if (!userRepository.getFriends(userId).contains(get(friendId))) {
            userRepository.getFriends(userId);
        }
        userRepository.removeFriend(userId, friendId);
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        log.info("GET /users/{id}/friends/common/{otherId}");
        return userRepository.getMutualFriends(userId, friendId);
    }

    private void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
