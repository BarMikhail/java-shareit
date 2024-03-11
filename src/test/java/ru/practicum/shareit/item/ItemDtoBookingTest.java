package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoBookingTest {

    @Autowired
    private JacksonTester<ItemDtoBooking> json;

    @Test
    void itemDtoBookingTest() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("Test !test test")
                .authorName("Appa")
                .created(LocalDateTime.now())
                .build();

        List<CommentDto> comments = new ArrayList<>(List.of(comment));


        BookingOwnerDto lastBooking = new BookingOwnerDto(1L, 2L, LocalDateTime.now(), LocalDateTime.now());
        BookingOwnerDto nextBooking = new BookingOwnerDto(2L, 3L, LocalDateTime.now(), LocalDateTime.now());

        ItemDtoBooking itemDto = ItemDtoBooking.builder()
                .id(1L)
                .name("Test")
                .description("test test")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();

        JsonContent<ItemDtoBooking> itemDtoExtendedSaved = json.write(itemDto);

        assertThat(itemDtoExtendedSaved).hasJsonPath("$.id");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.name");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.description");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.available");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.comments");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.lastBooking");
        assertThat(itemDtoExtendedSaved).hasJsonPath("$.nextBooking");

        assertThat(itemDtoExtendedSaved).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(itemDtoExtendedSaved).extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(itemDtoExtendedSaved).extractingJsonPathStringValue("$.description").isEqualTo("test test");
        assertThat(itemDtoExtendedSaved).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        assertThat(itemDtoExtendedSaved).extractingJsonPathArrayValue("$.comments").hasSize(1);

        assertThat(itemDtoExtendedSaved).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(1);
        assertThat(itemDtoExtendedSaved).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(2);

        assertThat(itemDtoExtendedSaved).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);
        assertThat(itemDtoExtendedSaved).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(3);
    }
}