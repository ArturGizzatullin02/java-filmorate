package ru.yandex.practicum.filmorate.repository.rating;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcRatingRepository.class, RatingRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcMpaRepository integration tests")
class JdbcRatingRepositoryTest {
    private static final int TEST_MPA_ID = 1;
    private static final int COUNT_OF_ELEMENTS = 5;

    private final JdbcRatingRepository jdbcRatingRepository;

    static Rating getTestRating() {
        Rating rating = new Rating();
        rating.setId(TEST_MPA_ID);
        rating.setName("G");
        return rating;
    }

    @Test
    @DisplayName("checkRatingExists() работает и проверяет наличие рейтинга")
    void checkRatingExists() {
        assertThat(jdbcRatingRepository.ratingExists(TEST_MPA_ID)).isTrue();
        assertThat(jdbcRatingRepository.ratingExists(COUNT_OF_ELEMENTS + 1)).isFalse();
    }

    @Test
    @DisplayName("get() возвращает корректный рейтинг")
    void get() {
        Optional<Rating> ratingOptional = jdbcRatingRepository.get(TEST_MPA_ID);

        assertThat(ratingOptional)
                .isPresent()
                .get()
                .extracting(Rating::getId, Rating::getName)
                .containsExactly(getTestRating().getId(), getTestRating().getName());
    }

    @Test
    @DisplayName("getAll() возвращает корректное количество рейтингов")
    void getAll() {
        assertThat(jdbcRatingRepository.getAll())
                .hasSize(COUNT_OF_ELEMENTS);
    }
}