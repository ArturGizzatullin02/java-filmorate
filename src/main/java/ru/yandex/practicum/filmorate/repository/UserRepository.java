package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserRepository {
    Map<Long, User> users = new HashMap<>();
    Map<Long, Set<Long>> userFriendsId = new HashMap<>();

    public void add(User user);

    public User get(long id);

    public Collection<User> getAll();

    public Map<Long, User> getMap();

    public List<User> getFriends(long userId);

    public Long getFriendById(long userId, long friendId);

    public List<User> addFriend(long userId, long friendId);

    public List<User> removeFriend(long userId, long friendId);

    public List<User> getMutualFriends(long userId, long friendId);

    public void remove(long id);

    public void update(User user);
}
