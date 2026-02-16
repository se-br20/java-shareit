package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired UserRepository repo;

    @Test
    void existsByEmailIgnoreCase_shouldWork() {
        repo.save(User.builder().name("n").email("A@A.RU").build());

        assertThat(repo.existsByEmailIgnoreCase("a@a.ru")).isTrue();
        assertThat(repo.existsByEmailIgnoreCase("x@x.ru")).isFalse();
    }
}
