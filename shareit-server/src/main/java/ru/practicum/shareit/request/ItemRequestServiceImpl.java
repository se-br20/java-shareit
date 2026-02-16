package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final Sort SORT_NEW_TO_OLD = Sort.by(Sort.Direction.DESC, "created");

    private final ItemRequestRepository requests;
    private final UserRepository users;
    private final ItemRepository items;

    @Override
    public ItemRequestDto create(Long userId, ItemRequestCreateDto dto) {
        User requester = requireUser(userId);

        String desc = dto != null ? dto.getDescription() : null;
        if (desc == null || desc.isBlank()) {
            throw new ValidationException("Request description must not be blank");
        }

        ItemRequest saved = requests.save(ItemRequest.builder()
                .description(desc.trim())
                .requester(requester)
                .created(LocalDateTime.now())
                .build());

        return ItemRequestMapper.toDto(saved, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getOwn(Long userId) {
        requireUser(userId);

        List<ItemRequest> list = requests.findByRequester_Id(userId, SORT_NEW_TO_OLD);
        if (list.isEmpty()) return List.of();

        return enrichWithItems(list);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getOthers(Long userId, int from, int size) {
        requireUser(userId);

        if (from < 0 || size <= 0) {
            throw new ValidationException("Invalid pagination params");
        }

        PageRequest page = PageRequest.of(from / size, size, SORT_NEW_TO_OLD);

        List<ItemRequest> list = requests.findByRequester_IdNot(userId, page);
        if (list.isEmpty()) return List.of();

        return enrichWithItems(list);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getById(Long userId, Long requestId) {
        requireUser(userId);

        ItemRequest req = requests.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found: " + requestId));

        List<ItemForRequestDto> itemDtos = items.findByRequestId(requestId).stream()
                .map(i -> new ItemForRequestDto(i.getId(), i.getName(), i.getOwner().getId()))
                .toList();

        return ItemRequestMapper.toDto(req, itemDtos);
    }

    private List<ItemRequestDto> enrichWithItems(List<ItemRequest> requestsList) {
        List<Long> requestIds = requestsList.stream().map(ItemRequest::getId).toList();

        List<Item> answerItems = items.findByRequestIdIn(requestIds);

        Map<Long, List<ItemForRequestDto>> itemsByRequest = answerItems.stream()
                .collect(Collectors.groupingBy(
                        Item::getRequestId,
                        Collectors.mapping(
                                i -> new ItemForRequestDto(i.getId(), i.getName(), i.getOwner().getId()),
                                Collectors.toList()
                        )
                ));

        return requestsList.stream()
                .map(r -> ItemRequestMapper.toDto(r, itemsByRequest.getOrDefault(r.getId(), List.of())))
                .toList();
    }

    private User requireUser(Long userId) {
        return users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }
}
