package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Comment;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {

    public static CommentDto toDto(Comment c) {
        if (c == null) return null;

        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setText(c.getText());
        dto.setAuthorName(c.getAuthor().getName());
        dto.setCreated(c.getCreated());
        return dto;
    }

    public static List<CommentDto> toDtoList(List<Comment> list) {
        if (list == null || list.isEmpty()) return List.of();
        return list.stream().map(CommentMapper::toDto).toList();
    }
}
