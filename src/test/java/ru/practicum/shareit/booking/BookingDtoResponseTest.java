package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.BookerDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoResponseTest {

    @Autowired
    private JacksonTester<BookingDtoResponse> jacksonTester;

    @Test
    void bookingDtoResponseTest() throws Exception {
        BookerDto bookerDto = BookerDto.builder()
                .id(1L)
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Test")
                .description("test test")
                .available(true)
                .owner(1L)
                .requestId(1L)
                .build();

        BookingDtoResponse bookingDtoResponse =
                BookingDtoResponse.builder()
                        .id(1L)
                        .booker(bookerDto)
                        .item(itemDto)
                        .end(LocalDateTime.now())
                        .start(LocalDateTime.now())
                        .status(BookingStatus.WAITING)
                        .build();

        JsonContent<BookingDtoResponse> bookingDtoOutputSaved = jacksonTester.write(bookingDtoResponse);

        assertThat(bookingDtoOutputSaved).hasJsonPath("$.id");
        assertThat(bookingDtoOutputSaved).hasJsonPath("$.start");
        assertThat(bookingDtoOutputSaved).hasJsonPath("$.end");
        assertThat(bookingDtoOutputSaved).hasJsonPath("$.item");
        assertThat(bookingDtoOutputSaved).hasJsonPath("$.booker");
        assertThat(bookingDtoOutputSaved).hasJsonPath("$.status");

        assertThat(bookingDtoOutputSaved).extractingJsonPathNumberValue("$.id").isEqualTo(1);

        assertThat(bookingDtoOutputSaved).hasJsonPathValue("$.start");
        assertThat(bookingDtoOutputSaved).hasJsonPathValue("$.end");

        assertThat(bookingDtoOutputSaved).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(bookingDtoOutputSaved).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(bookingDtoOutputSaved).extractingJsonPathStringValue("$.item.name").isEqualTo(itemDto.getName());
        assertThat(bookingDtoOutputSaved).extractingJsonPathStringValue("$.status").isEqualTo(bookingDtoResponse.getStatus().toString());
    }
}