package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void bookingDtoTest() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.itemId");

        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}