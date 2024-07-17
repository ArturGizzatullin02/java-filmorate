package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collection;
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
        return userRepository.get(id);
    }

    public Set<Long> getFriendsIds(long id) {
        return userRepository.getFriendsIds(id);
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

    public void addFriend(long userId, long friendId) {
        userRepository.addFriend(userId, friendId);
    }

    public Long removeFriend(long userId, long friendId) {
        userRepository.removeFriend(userId, friendId);
        return userRepository.getFriendById(userId, friendId);
    }

    public Set<Long> getMutualFriends(long userId, long friendId) {
        return userRepository.getMutualFriends(userId, friendId);
    }
}
