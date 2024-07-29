package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper mapper;

    @Override
    public Optional<Genre> get(long id) {
        String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = :GENRE_ID ORDER BY GENRE_ID;";
        MapSqlParameterSource paramSource = new MapSqlParameterSource("GENRE_ID", id);

        List<Genre> result = jdbc.query(sqlQuery, paramSource, mapper);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.getFirst());
        }
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbc.query("SELECT * FROM GENRES", mapper);
    }

    @Override
    public boolean genreExists(long id) {
        return get(id).isPresent();
    }

    @Override
    public boolean genreExists(List<Long> ids) {
        int genresIdsSize = ids.size();
        if (genresIdsSize == 0) return true;

        return genresIdsSize == Optional.ofNullable(jdbc.queryForObject("SELECT COUNT(*) " +
                        "FROM GENRES WHERE GENRE_ID IN (:GENRE_IDS);",
                new MapSqlParameterSource("GENRE_IDS", ids), Integer.class)).orElse(0);
    }
}
