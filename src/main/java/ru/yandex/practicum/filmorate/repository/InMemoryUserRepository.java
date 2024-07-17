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

    //    @Override
//    public void addFriend(long userId, long friendId) {
//        Set<Long> userFriends = userFriendsId.computeIfAbsent(userId, id -> new HashSet<>());
//        userFriends.add(friendId);
//
//        Set<Long> friendFriendsId = userFriendsId.computeIfAbsent(friendId, id -> new HashSet<>());
//        friendFriendsId.add(userId);
//    }
    @Override
    public List<User> addFriend(long userId, long friendId) {
        if (userFriendsId.get(userId) != null) {
            userFriendsId.get(userId).add(friendId);
        } else {
            userFriendsId.put(userId, new HashSet<>());
            userFriendsId.get(userId).add(friendId);
        }
        if (userFriendsId.get(friendId) != null) {
            userFriendsId.get(friendId).add(userId);
        } else {
            userFriendsId.put(friendId, new HashSet<>());
            userFriendsId.get(friendId).add(userId);
        }
        List<Long> userFriendsIdList = new ArrayList<>(userFriendsId.get(userId));
        List<User> userFriendsList = new ArrayList<>();
        for (Long id : userFriendsIdList) {
            userFriendsList.add(users.get(id));
        }
        return userFriendsList;
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
    public List<User> getFriends(long userId) {
        Set<Long> friendsId = userFriendsId.get(userId);
        if (friendsId == null) {
            return Collections.emptyList();
        }
        List<Long> userFriendsIdList = new ArrayList<>(friendsId);
        List<User> userFriendsList = new ArrayList<>();
        for (Long id : userFriendsIdList) {
            userFriendsList.add(users.get(id));
        }
        return userFriendsList;
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
    public List<User> getMutualFriends(long userId, long friendId) {
        Set<Long> commonFriends = new HashSet<>();
        Set<Long> userFriends = userFriendsId.computeIfAbsent(userId, id -> new HashSet<>());
        Set<Long> friendFriends = userFriendsId.computeIfAbsent(friendId, id -> new HashSet<>());
        for (Long id : userFriends) {
            if (friendFriends.contains(id)) {
                commonFriends.add(id);
            }
        }
        List<Long> userFriendsIdList = new ArrayList<>(commonFriends);
        List<User> userFriendsList = new ArrayList<>();
        for (Long id : userFriendsIdList) {
            userFriendsList.add(users.get(id));
        }
        return userFriendsList;
    }

    @Override
    public void remove(long id) {
        users.remove(id);
    }

    @Override
    public List<User> removeFriend(long userId, long friendId) {
        if (userFriendsId.get(userId).contains(friendId)) {
            userFriendsId.get(userId).remove(friendId);
            userFriendsId.get(friendId).remove(userId);
            List<Long> userFriendsIdList = new ArrayList<>(userFriendsId.get(userId));
            List<User> userFriendsList = new ArrayList<>();
            for (Long id : userFriendsIdList) {
                userFriendsList.add(users.get(id));
            }
            return userFriendsList;
        }
        return Collections.emptyList();
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    private long nextId() {
        return id++;
    }
}
