package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    public void add(User user);

    public Optional<User> get(long id);

    public Collection<User> getAll();

    boolean userExists(long id);

    public List<User> getFriends(long userId);

    public Long getFriendById(long userId, long friendId);

    public List<User> addFriend(long userId, long friendId);

    public List<User> removeFriend(long userId, long friendId);

    public List<User> getMutualFriends(long userId, long friendId);

    public void remove(long id);

    public void update(User user);
}
