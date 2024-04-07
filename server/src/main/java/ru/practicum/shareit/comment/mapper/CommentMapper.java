package ru.practicum.shareit.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }

    public Comment toCommentRequest(CommentRequestDto commentRequestDto) {
        return Comment.builder()
                .text(commentRequestDto.getText())
                .build();
    }
}
