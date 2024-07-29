package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    User add(User user);

    Optional<User> get(long id);

    Collection<User> getAll();

    boolean userExists(long id);

    List<User> getFriends(long userId);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    List<User> getMutualFriends(long userId, long friendId);

    void remove(long id);

    void update(User user);
}
