package ru.yandex.practicum.filmorate.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    Map<Long, User> users = new HashMap<>();
    Map<Long, Set<Long>> userFriendsId = new HashMap<>();

    private long id = 1;

    @Override
    public void add(User user) {
        user.setId(nextId());
        users.put(user.getId(), user);
    }

    @Override
    public List<User> addFriend(long userId, long friendId) {
        final Set<Long> listOfUserFriendsId = userFriendsId.computeIfAbsent(userId, k -> new HashSet<>());
        listOfUserFriendsId.add(friendId);
        final Set<Long> listOfFriendFriendsId = userFriendsId.computeIfAbsent(friendId, k -> new HashSet<>());
        listOfFriendFriendsId.add(userId);
        return getUsers(userId);
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.of(users.get(id));
    }

    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean userExists(long id) {
        return users.containsKey(id);
    }

    @Override
    public List<User> getFriends(long userId) {
        Set<Long> friendsId = userFriendsId.get(userId);
        if (friendsId == null) {
            return Collections.emptyList();
        }
        return getUsers(friendsId);
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
        Set<Long> userFriends = userFriendsId.get(userId);
        Set<Long> friendFriends = userFriendsId.get(friendId);
        if (friendFriends != null && userFriends != null) {
            for (Long id : userFriends) {
                if (friendFriends.contains(id)) {
                    commonFriends.add(id);
                }
            }
        }
        return getUsers(commonFriends);
    }

    private List<User> getUsers(Set<Long> commonFriends) {
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
            return getUsers(userId);
        }
        return Collections.emptyList();
    }

    private List<User> getUsers(long userId) {
        List<Long> userFriendsIdList = new ArrayList<>(userFriendsId.get(userId));
        List<User> userFriendsList = new ArrayList<>();
        for (Long id : userFriendsIdList) {
            userFriendsList.add(users.get(id));
        }
        return userFriendsList;
    }

    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }

    private long nextId() {
        return id++;
    }
}
