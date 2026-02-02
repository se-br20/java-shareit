package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public User save(User user) {
        long id = seq.incrementAndGet();
        User saved = User.builder()
                .id(id)
                .name(user.getName())
                .email(user.getEmail())
                .build();
        users.put(id, saved);
        return saved;
    }

    @Override
    public User update(User user) {
        Long id = user.getId();
        if (id == null || !users.containsKey(id)) {
            throw new NotFoundException("User not found: " + id);
        }
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public void deleteById(Long id) {
        if (users.remove(id) == null) {
            throw new NotFoundException("User not found: " + id);
        }
    }

    @Override
    public boolean existsByEmail(String email, Long excludeUserId) {
        if (email == null) return false;
        String target = email.trim();
        if (target.isBlank()) return false;

        return users.values().stream().anyMatch(u -> {
            if (u == null || u.getEmail() == null) return false;
            if (excludeUserId != null && excludeUserId.equals(u.getId())) return false;
            return u.getEmail().trim().equalsIgnoreCase(target);
        });
    }
}