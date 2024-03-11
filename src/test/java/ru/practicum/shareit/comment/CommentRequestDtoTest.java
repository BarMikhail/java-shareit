package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.comment.dto.CommentRequestDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentRequestDtoTest {
    @Autowired
    private JacksonTester<CommentRequestDto> json;

    @Test
    void commentRequestDtoTest() throws Exception {
        CommentRequestDto bookerDto = CommentRequestDto.builder()
                .text("test")
                .build();

        JsonContent<CommentRequestDto> result = json.write(bookerDto);
        assertThat(result).hasJsonPath("$.text");

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");

    }

}