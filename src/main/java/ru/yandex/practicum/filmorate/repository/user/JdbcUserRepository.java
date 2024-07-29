package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper mapper;

    @Override
    public void add(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "VALUES (:EMAIL, :LOGIN, :USER_NAME, :BIRTHDAY);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sqlQuery, user.toMap(), keyHolder);
        user.setId(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public boolean userExists(long userId) {
        String sqlQuery = "SELECT COUNT(*) FROM USERS WHERE USER_ID = :USER_ID;";
        return Optional.ofNullable(jdbc.queryForObject(sqlQuery,
                new MapSqlParameterSource("USER_ID", userId), Integer.class)).isPresent();
    }

    @Override
    public Optional<User> get(long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = :USER_ID;";
        MapSqlParameterSource paramSource = new MapSqlParameterSource("USER_ID", userId);

        List<User> result = jdbc.query(sqlQuery, paramSource, mapper);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.getFirst());
        }
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS;";

        return jdbc.query(sqlQuery, mapper);
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE USERS SET EMAIL = :EMAIL, LOGIN = :LOGIN, USER_NAME = :USER_NAME," +
                " BIRTHDAY = :BIRTHDAY WHERE USER_ID = :USER_ID;";

        jdbc.update(sqlQuery, user.toMap());
    }

    @Override
    public void remove(long userId) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID = :USER_ID;";
        jdbc.update(sqlQuery, new MapSqlParameterSource("USER_ID", userId));
    }

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_1_ID, USER_2_ID) VALUES (:USER_1_ID, :USER_2_ID);";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_1_ID", userId);
        params.addValue("USER_2_ID", friendId);

        jdbc.update(sqlQuery, params);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USERS.USER_ID IN " +
                "(SELECT USER_2_ID FROM FRIENDSHIPS WHERE USER_1_ID = :USER_1_ID);";

        return jdbc.query(sqlQuery, new MapSqlParameterSource("USER_1_ID", userId), mapper);
    }

    @Override
    public List<User> getMutualFriends(long userId, long friendId) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID IN " +
                "(SELECT USER_2_ID FROM FRIENDSHIPS WHERE USER_1_ID = :USER_1_ID " +
                "INTERSECT " +
                "SELECT USER_2_ID FROM FRIENDSHIPS WHERE USER_1_ID = :USER_2_ID);";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_1_ID", userId);
        params.addValue("USER_2_ID", friendId);

        return jdbc.query(sqlQuery, params, mapper);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_1_ID = :USER_1_ID AND USER_2_ID = :USER_2_ID;";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_1_ID", userId);
        params.addValue("USER_2_ID", friendId);

        jdbc.update(sqlQuery, params);
    }
}