package ru.yandex.practicum.filmorate.repository.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcRatingRepository implements RatingRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final RatingRowMapper mapper;

    @Override
    public Optional<Rating> get(long id) {
        String sqlQuery = "SELECT * FROM RATINGS WHERE RATING_ID = :RATING_ID";
        MapSqlParameterSource paramSource = new MapSqlParameterSource("RATING_ID", id);

        List<Rating> result = jdbc.query(sqlQuery, paramSource, mapper);
        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.getFirst());
        }
    }

    @Override
    public Collection<Rating> getAll() {
        return jdbc.query("SELECT * FROM RATINGS", mapper);
    }

    @Override
    public boolean ratingExists(List<Long> ids) {
        int genresIdsSize = ids.size();
        if (genresIdsSize == 0) return true;

        List<Rating> result = jdbc.query("SELECT COUNT(*) " +
                        "FROM RATINGS WHERE RATING_ID IN (:RATING_IDS);",
                new MapSqlParameterSource("RATING_IDS", ids), mapper);

        return genresIdsSize == result.size();
    }

    @Override
    public boolean ratingExists(long id) {
        return get(id).isPresent();
    }
}
