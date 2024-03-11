package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingOwnerDtoTest {

    @Autowired
    private JacksonTester<BookingOwnerDto> json;

    @Test
    void bookingOwnerDtoTest() throws Exception {
        BookingOwnerDto bookingOwnerDto = BookingOwnerDto.builder()
                .id(1L)
                .bookerId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();


        JsonContent<BookingOwnerDto> result = json.write(bookingOwnerDto);

        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.bookerId");
        assertThat(result).hasJsonPath("$.id");

        assertThat(result).hasJsonPathValue("$.start");
        assertThat(result).hasJsonPathValue("$.end");
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

}