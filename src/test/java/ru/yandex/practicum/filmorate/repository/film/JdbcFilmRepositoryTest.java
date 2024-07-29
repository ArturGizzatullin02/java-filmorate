package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcFilmRepository.class, FilmRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository integration tests")
class JdbcFilmRepositoryTest {
    private static final long TEST_FILM_ID = 1L;
    private static final long COUNT_OF_ELEMENTS = 3L;
    private static final long TEST_USER_ID = 1L;

    private final JdbcFilmRepository jdbcFilmRepository;

    static Film getTestFilm() {
        Film film = new Film();
        film.setId(TEST_FILM_ID);
        film.setName("troya");
        film.setDescription("brave men");
        film.setReleaseDate(LocalDate.parse("1998-06-17"));
        film.setDuration(90);
        film.setMpa(new Rating(5, "NC-17"));

        return film;
    }

    static Film getTestNewFilm() {
        Film film = new Film();
        film.setName("vampire");
        film.setDescription("sumerki");
        film.setReleaseDate(LocalDate.parse("2017-02-08"));
        film.setDuration(100);
        film.setMpa(new Rating(2, "PG"));

        return film;
    }

    @Test
    @DisplayName("get() возвращает корректное значение")
    void get() {
        Optional<Film> film = jdbcFilmRepository.get(TEST_FILM_ID);

        assertThat(film)
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestFilm());
    }

    @Test
    @DisplayName("getAll() возврашает корректное число элементов")
    void getAll() {
        assertThat(jdbcFilmRepository.getAll())
                .hasSize((int) COUNT_OF_ELEMENTS);
    }

    @Test
    @DisplayName("create() создает такой же фильм в БД. ID не считаются")
    void create() {
        Film createdFilm = jdbcFilmRepository.add(getTestNewFilm());

        assertThat(jdbcFilmRepository.get(createdFilm.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(getTestNewFilm());
    }

    @Test
    @DisplayName("update() корректно обновляет значения")
    void update() {
        Film updateFilm = getTestNewFilm();
        updateFilm.setId(TEST_FILM_ID);

        assertThat(jdbcFilmRepository.get(updateFilm.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isNotEqualTo(updateFilm);

        jdbcFilmRepository.update(updateFilm);

        assertThat(jdbcFilmRepository.get(updateFilm.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestNewFilm());
    }

    @Test
    @DisplayName("delete() корректно удаляет пользователя из списка друзей")
    void delete() {
        assertThat(jdbcFilmRepository.get(TEST_FILM_ID)).isPresent();
        jdbcFilmRepository.remove(TEST_FILM_ID);
        assertThat(jdbcFilmRepository.get(TEST_FILM_ID)).isEmpty();
    }
}