package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryItemRepository implements ItemRepository {

    private final Map<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public Item save(Item item) {
        long id = seq.incrementAndGet();
        Item saved = Item.builder()
                .id(id)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .build();
        items.put(id, saved);
        return saved;
    }

    @Override
    public Item update(Item item) {
        Long id = item.getId();
        if (id == null || !items.containsKey(id)) {
            throw new NotFoundException("Item not found: " + id);
        }
        items.put(id, item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(i -> ownerId.equals(i.getOwnerId()))
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    @Override
    public List<Item> searchAvailable(String text) {
        String q = text == null ? "" : text.trim().toLowerCase();
        if (q.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .filter(i -> Boolean.TRUE.equals(i.getAvailable()))
                .filter(i -> containsIgnoreCase(i.getName(), q) || containsIgnoreCase(i.getDescription(), q))
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    private boolean containsIgnoreCase(String value, String qLower) {
        if (value == null) return false;
        return value.toLowerCase().contains(qLower);
    }
}
