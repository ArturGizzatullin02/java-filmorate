package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        return userRepository.get(id);
    }

    public List<User> getFriends(long id) {
        log.info("GET /users{id}");
        if (userRepository.get(id) == null) {
            throw new NotFoundException("Пользователь с данным ID не найден");
        }
        return userRepository.getFriends(id);
    }

    public User add(User user) {
        log.info("POST /users ==> Started");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userRepository.add(user);
        log.info("POST /users ==> Finished");
        return user;
    }

    public User update(User user) {
        log.info("PUT /users ==> Started");
        if (userRepository.getMap().containsKey(user.getId())) {
            userRepository.update(user);
            log.info("PUT /users ==> Finished");
        } else {
            log.info("PUT /users ==> User not found");
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        return user;
    }

    public List<User> addFriend(long userId, long friendId) {
        log.info("PUT /users/{id}/friends/{friendId}");
        if (userRepository.get(friendId) == null) {
            throw new NotFoundException("Друг с таким ID не найден");
        }
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        return userRepository.addFriend(userId, friendId);
    }

    public List<User> removeFriend(long userId, long friendId) {
        log.info("DELETE /users/{id}/friends/{friendId}");
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("Пользователь с указанным ID не найден");
        }
        if (userRepository.get(friendId) == null) {
            throw new NotFoundException("Пользователь с указанным ID не найден");
        }
        if (!userRepository.getFriends(userId).contains(userRepository.get(friendId))) {
            return userRepository.getFriends(userId);
        }
        return userRepository.removeFriend(userId, friendId);
    }

    public List<User> getMutualFriends(long userId, long friendId) {
        log.info("GET /users/{id}/friends/common/{otherId}");
        return userRepository.getMutualFriends(userId, friendId);
    }
}
