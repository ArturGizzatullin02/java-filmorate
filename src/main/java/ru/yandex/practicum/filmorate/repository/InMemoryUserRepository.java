package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    long id = 1;

    @Override
    public void add(User user) {
        user.setId(nextId());
        users.put(user.getId(), user);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        Set<Long> userFriends = userFriendsId.computeIfAbsent(userId, id -> new HashSet<>());
        userFriends.add(friendId);

        Set<Long> friendFriendsId = userFriendsId.computeIfAbsent(friendId, id -> new HashSet<>());
        friendFriendsId.add(userId);
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Map<Long, User> getMap() {
        return users;
    }

    @Override
    public Set<Long> getFriendsId(long userId) {
        return userFriendsId.get(userId);
    }

    @Override
    public Long getFriendById(long userId, long friendId) {
        for (Long id : userFriendsId.get(userId)) {
            if (id.equals(friendId)) {
                return id;
            }
        }
        throw new NotFoundException("Друг с таким id не найден");
    }

    @Override
    public Set<Long> getMutualFriends(long userId, long friendId) {
        Set<Long> commonFriends = new HashSet<>();
        Set<Long> userFriends = userFriendsId.computeIfAbsent(userId, id -> new HashSet<>());
        Set<Long> friendFriends = userFriendsId.computeIfAbsent(friendId, id -> new HashSet<>());
        for (Long id : userFriends) {
            if (friendFriends.contains(id)) {
                commonFriends.add(id);
            }
        }
        return commonFriends;
    }

    @Override
    public void remove(long id) {
        users.remove(id);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userFriendsId.get(userId).remove(friendId);
        userFriendsId.get(friendId).remove(userId);
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    private long nextId() {
        return id++;
    }
}
