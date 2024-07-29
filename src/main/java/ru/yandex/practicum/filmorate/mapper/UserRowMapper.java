package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setEmail(rs.getString("EMAIL"));
        user.setLogin(rs.getString("LOGIN"));
        user.setName(rs.getString("USER_NAME"));
        user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

    public MapSqlParameterSource toMap(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_ID", user.getId());
        params.addValue("EMAIL", user.getEmail());
        params.addValue("LOGIN", user.getLogin());
        params.addValue("USER_NAME", user.getName());
        params.addValue("BIRTHDAY", Date.valueOf(user.getBirthday()));
        return params;
    }
}
