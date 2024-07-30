package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film>, ResultSetExtractor<Collection<Film>> {
    private final RatingRowMapper ratingRowMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(ratingRowMapper.mapRow(rs, rowNum));
        return film;
    }

    @Override
    public Collection<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Collection<Film> films = new LinkedList<>();

        Film film = null;
        while (rs.next()) {
            if (film == null) {
                film = mapRow(rs, rs.getRow());
            } else if (!film.getId().equals(rs.getLong("FILM_ID"))) {
                films.add(film);
                film = mapRow(rs, rs.getRow());
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

    public MapSqlParameterSource toMap(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("FILM_ID", film.getId());
        params.addValue("FILM_NAME", film.getName());
        params.addValue("DESCRIPTION", film.getDescription());
        params.addValue("RELEASE_DATE", Date.valueOf(film.getReleaseDate()));
        params.addValue("DURATION", film.getDuration());
        params.addValue("RATING_ID", film.getMpa().getId());
        return params;
    }
}
