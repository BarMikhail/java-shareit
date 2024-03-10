package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.BookerDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class BookerDtoTest {

    @Autowired
    private JacksonTester<BookerDto> json;

    @Test
    void testUserDto() throws Exception {
        BookerDto bookerDto = BookerDto.builder()
                .id(1L)
                .build();

        JsonContent<BookerDto> result = json.write(bookerDto);
        assertThat(result).hasJsonPath("$.id");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);

    }
}