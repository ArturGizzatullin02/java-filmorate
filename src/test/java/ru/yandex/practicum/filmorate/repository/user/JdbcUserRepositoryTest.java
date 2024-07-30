package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcUserRepository.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository integration tests")
class JdbcUserRepositoryTest {
    private static final long TEST_USER_ID = 1L;
    private static final long COUNT_OF_ELEMENTS = 2L;

    private final JdbcUserRepository jdbcUserRepository;

    static User getTestUser() {
        User user = new User();
        user.setId(TEST_USER_ID);
        user.setEmail("vladimirov@gmail.com");
        user.setLogin("Vladimir");
        user.setName("Vladimirov");
        user.setBirthday(LocalDate.parse("1998-03-17"));
        return user;
    }

    static User getTestNewUser() {
        User user = new User();
        user.setEmail("ivanov@gmail.com");
        user.setLogin("Ivanov");
        user.setName("Ivan");
        user.setBirthday(LocalDate.parse("2005-07-27"));
        return user;
    }

    @Test
    @DisplayName("get() возвращает корретное значение")
    void get() {
        Optional<User> user = jdbcUserRepository.get(TEST_USER_ID);

        assertThat(user)
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestUser());
    }

    @Test
    @DisplayName("getAll() возвращает корретное количество элементов")
    void getAll() {
        assertThat(jdbcUserRepository.getAll())
                .hasSize((int) COUNT_OF_ELEMENTS);
    }

    @Test
    @DisplayName("create() создает такого же пользователя в БД. ID не считаются")
    void create() {
        User createdUser = jdbcUserRepository.add(getTestNewUser());

        assertThat(jdbcUserRepository.get(createdUser.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(getTestNewUser());
    }

    @Test
    @DisplayName("update() корректно обновляет значения")
    void update() {
        User updateUser = getTestNewUser();
        updateUser.setId(TEST_USER_ID);

        assertThat(jdbcUserRepository.get(updateUser.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isNotEqualTo(updateUser);

        jdbcUserRepository.update(updateUser);

        assertThat(jdbcUserRepository.get(updateUser.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(getTestNewUser());
    }

    @Test
    @DisplayName("getMutualFriends() возвращает список общих друзей пользователя")
    void getMutualFriends() {
        User friend = jdbcUserRepository.add(getTestNewUser());

        jdbcUserRepository.addFriend(TEST_USER_ID, friend.getId());
        jdbcUserRepository.addFriend(COUNT_OF_ELEMENTS, friend.getId());

        assertThat(jdbcUserRepository.getMutualFriends(TEST_USER_ID, COUNT_OF_ELEMENTS))
                .singleElement()
                .isEqualTo(friend);
    }

    @Test
    @DisplayName("removeFriend() корректно удаляет пользователя из списка друзей")
    void removeFriend() {
        jdbcUserRepository.addFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);
        assertThat(jdbcUserRepository.getFriends(TEST_USER_ID)).hasSize(1);
        jdbcUserRepository.removeFriend(TEST_USER_ID, COUNT_OF_ELEMENTS);
        assertThat(jdbcUserRepository.getFriends(TEST_USER_ID)).hasSize(0);
    }
}