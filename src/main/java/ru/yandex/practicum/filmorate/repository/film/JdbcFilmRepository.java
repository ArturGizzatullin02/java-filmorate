package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper mapper;

    private class FilmsResultSetExtractor implements ResultSetExtractor<Collection<Film>> {
        @Override
        public Collection<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Collection<Film> films = new LinkedList<>();

            Film film = null;
            while (rs.next()) {
                if (film == null) {
                    film = mapper.mapRow(rs, rs.getRow());
                } else if (!film.getId().equals(rs.getLong("FILM_ID"))) {
                    films.add(film);
                    film = mapper.mapRow(rs, rs.getRow());
                }
                long genreId = rs.getInt("GENRE_ID");
                if (!rs.wasNull()) {
                    assert film != null;
                    film.getGenres().add(new Genre(genreId, rs.getString("GENRE_NAME")));
                }
            }
            if (film != null) {
                films.add(film);
            }
            return films;
        }
    }

    private void setFilmGenres(Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = :FILM_ID;";
        jdbc.update(sqlQuery, new MapSqlParameterSource("FILM_ID", film.getId()));

        sqlQuery = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:FILM_ID, :GENRE_ID);";
        SqlParameterSource[] batchParams = SqlParameterSourceUtils.createBatch(
                film.getGenres().stream().map(genre -> {
                    Map<String, Object> param = new HashMap<>();
                    param.put("FILM_ID", film.getId());
                    param.put("GENRE_ID", genre.getId());
                    return param;
                }).toList()
        );
        jdbc.batchUpdate(sqlQuery, batchParams);
    }

    @Override
    public Film add(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)" +
                        " VALUES (:FILM_NAME, :DESCRIPTION, :RELEASE_DATE, :DURATION, :RATING_ID);",
                film.toMap(), keyHolder, new String[]{"FILM_ID"});
        film.setId(keyHolder.getKeyAs(Long.class));
        setFilmGenres(film);
        return film;
    }

    @Override
    public boolean filmExists(long id) {
        String sqlQuery = "SELECT COUNT(*) FROM FILMS WHERE FILM_ID = :FILM_ID;";
        return Optional.ofNullable(jdbc.queryForObject(sqlQuery, new MapSqlParameterSource("FILM_ID", id),
                Long.class)).isPresent();
    }

    @Override
    public Optional<Film> get(long id) {
        return jdbc.query("SELECT f.*, r.RATING_NAME AS rating_name, g.GENRE_ID, g.GENRE_NAME AS genre_name " +
                "FROM FILMS AS f LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "WHERE f.FILM_ID = :FILM_ID;", new MapSqlParameterSource("FILM_ID", id),
                rs -> {
                    Film film = null;
                    while (rs.next()) {
                        if (film == null) {
                            film = mapper.mapRow(rs, rs.getRow());
                        }
                        long genreId = rs.getInt("GENRE_ID");
                        if (!rs.wasNull()) {
                            assert film != null;
                            film.getGenres().add(new Genre(genreId, rs.getString("GENRE_NAME")));
                        }
                    }
                    return Optional.ofNullable(film);
                });
    }

    @Override
    public Collection<Film> getAll() {
        String sqlQuery = "SELECT f.*, r.RATING_NAME AS rating_name, g.GENRE_ID, g.GENRE_NAME AS genre_name " +
                "FROM FILMS AS f LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "ORDER BY f.FILM_ID;";
        return jdbc.query(sqlQuery, new FilmsResultSetExtractor());
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET FILM_NAME = :FILM_NAME, DESCRIPTION = :DESCRIPTION," +
                " RELEASE_DATE = :RELEASE_DATE, DURATION = :DURATION, RATING_ID = :RATING_ID " +
                "WHERE FILM_ID = :FILM_ID;";
        jdbc.update(sqlQuery, film.toMap());
        setFilmGenres(film);

        return film;
    }

    @Override
    public void remove(long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = :FILM_ID;";
        jdbc.update(sqlQuery, new MapSqlParameterSource("FILM_ID", id));
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "MERGE INTO LIKES (USER_ID, FILM_ID) VALUES (:USER_ID, :FILM_ID);";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_ID", userId);
        params.addValue("FILM_ID", filmId);

        jdbc.update(sqlQuery, params);
    }

    @Override
    public List<Film> getMostPopulars(int count) {
        String sqlQuery = "SELECT f.*, r.RATING_NAME AS rating_name, g.GENRE_ID, " +
                "g.GENRE_NAME AS genre_name FROM " +
                "(SELECT FILM_ID, COUNT(*) AS likesCount FROM LIKES " +
                "GROUP BY FILM_ID ORDER BY likesCount DESC LIMIT :COUNT) AS p " +
                "JOIN FILMS AS f ON p.FILM_ID = f.FILM_ID " +
                "LEFT JOIN RATINGS AS r ON f.RATING_ID = r.RATING_ID " +
                "LEFT JOIN FILM_GENRE AS fg ON f.FILM_ID = fg.FILM_ID " +
                "LEFT JOIN GENRES AS g ON fg.GENRE_ID = g.GENRE_ID " +
                "ORDER BY p.likesCount DESC, f.FILM_ID;";

        return (List<Film>) jdbc.query(sqlQuery, new MapSqlParameterSource("COUNT", count),
                new FilmsResultSetExtractor());
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE USER_ID = :USER_ID AND FILM_ID = :FILM_ID;";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("USER_ID", userId);
        params.addValue("FILM_ID", filmId);
        jdbc.update(sqlQuery, params);
    }
}