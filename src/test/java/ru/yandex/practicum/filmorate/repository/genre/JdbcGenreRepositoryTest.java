package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@Import({JdbcGenreRepository.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcGenreRepository integration tests")
class JdbcGenreRepositoryTest {
    private static final long TEST_GENRE_ID = 1;
    private static final long COUNT_OF_ELEMENTS = 6;

    private final JdbcGenreRepository jdbcGenreRepository;

    static Genre getTestGenre() {
        Genre genre = new Genre();
        genre.setId(TEST_GENRE_ID);
        genre.setName("Комедия");
        return genre;
    }

    @Test
    @DisplayName("checkGenreExists() работает и проверяет наличие элемента в списке")
    void checkGenreExists() {
        assertThat(jdbcGenreRepository.genreExists(TEST_GENRE_ID)).isTrue();
        assertThat(jdbcGenreRepository.genreExists(COUNT_OF_ELEMENTS + 1)).isFalse();
    }

    @Test
    @DisplayName("checkGenresExist() проверяет наличие элементов в списке, пустые обозначает")
    void checkGenresExist() {
        assertThat(jdbcGenreRepository.genreExists(List.of())).isTrue();
        assertThat(jdbcGenreRepository.genreExists(List.of(TEST_GENRE_ID, COUNT_OF_ELEMENTS))).isTrue();
        assertThat(jdbcGenreRepository.genreExists(List.of(TEST_GENRE_ID, COUNT_OF_ELEMENTS + 1))).isFalse();
    }

    @Test
    @DisplayName("get() возвращает корректное значение")
    void get() {
        Optional<Genre> genre = jdbcGenreRepository.get(TEST_GENRE_ID);

        assertThat(genre)
                .isPresent()
                .get()
                .extracting(Genre::getId, Genre::getName)
                .containsExactly(getTestGenre().getId(), getTestGenre().getName());
    }

    @Test
    @DisplayName("getAll() возвращает корректное значение элементов")
    void getAll() {
        assertThat(jdbcGenreRepository.getAll())
                .hasSize((int) COUNT_OF_ELEMENTS);
    }
}