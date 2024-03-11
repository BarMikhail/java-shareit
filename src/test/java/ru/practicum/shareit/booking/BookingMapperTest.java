package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {



    private User owner = User.builder()
            .id(1L)
            .name("Test")
            .email("test@ya.ru")
            .build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Looking for a balalaika")
            .build();

    private Item item =Item.builder()
            .id(1L)
            .available(true)
            .description("test test")
            .owner(owner)
            .itemRequest(itemRequest)
            .name("test")
            .build();

    private User user = User.builder()
            .id(1L)
            .name("Test")
            .email("test@ya.ru")
            .build();

    private Booking booking =Booking.builder().id(1L)
            .booker(owner)
            .item(item)
            .endDate(LocalDateTime.now())
            .startDate(LocalDateTime.now())
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void toBooking() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        Booking booking = BookingMapper.toBooking(bookingDto,item,user);

        assertNotNull(booking);

        assertEquals(booking.getBooker(), user);
        assertEquals(booking.getItem(), item);
        assertEquals(booking.getStartDate(), bookingDto.getStart());
        assertEquals(booking.getEndDate(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), BookingStatus.WAITING);
    }

    @Test
    void toBookingResponseTest() {

        ItemDto itemDto = ItemMapper.toItemDTO(item);

        BookingDtoResponse bookingDtoResponse = BookingMapper.toBookingResponse(booking,itemDto);

        assertNotNull(bookingDtoResponse);

        assertEquals(booking.getId(), bookingDtoResponse.getId());
        assertEquals(UserMapper.toBookerDto(booking.getBooker()), bookingDtoResponse.getBooker());
        assertEquals(itemDto, bookingDtoResponse.getItem());
        assertEquals(booking.getStartDate(), bookingDtoResponse.getStart());
        assertEquals(booking.getEndDate(), bookingDtoResponse.getEnd());
        assertEquals(bookingDtoResponse.getStatus(), BookingStatus.WAITING);

    }

    @Test
    void toBookingOwnerDtoTest() {

        BookingOwnerDto bookingOwnerDto = BookingMapper.toBookingOwnerDto(booking);

        assertNotNull(bookingOwnerDto);

        assertEquals(booking.getId(), bookingOwnerDto.getId());
        assertEquals(booking.getBooker().getId(), bookingOwnerDto.getBookerId());
        assertEquals(booking.getStartDate(), bookingOwnerDto.getStart());
        assertEquals(booking.getEndDate(), bookingOwnerDto.getEnd());

    }

}