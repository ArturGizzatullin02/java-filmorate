package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    void add(User user);

    Optional<User> get(long id);

    Collection<User> getAll();

    boolean userExists(long id);

    List<User> getFriends(long userId);

    List<User> addFriend(long userId, long friendId);

    List<User> removeFriend(long userId, long friendId);

    List<User> getMutualFriends(long userId, long friendId);

    void remove(long id);

    void update(User user);
}
