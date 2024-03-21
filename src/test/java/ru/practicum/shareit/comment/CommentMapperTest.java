package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentMapperTest {

    @Test
    void toCommentRequestTest() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("Test")
                .build();

        Comment comment = CommentMapper.toCommentRequest(commentRequestDto);

        assertNotNull(comment);

        assertEquals(comment.getText(), commentRequestDto.getText());
    }

    @Test
    void toCommentDtoTest() {
        User user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("test")
                .user(user)
                .build();

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        assertNotNull(comment);

        assertEquals(comment.getText(), commentDto.getText());
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(user.getName(), commentDto.getAuthorName());
    }
}
