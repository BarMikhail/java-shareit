package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingOwnerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public Booking toBooking(BookingDto bookingDto, Item item, User booker){
        return Booking.builder()
                .booker(booker)
                .item(item)
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }

    public BookingDtoResponse toBookingResponse(Booking booking, ItemDto itemDto){
        return BookingDtoResponse.builder()
                .id(booking.getId())
                .item(itemDto)
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .booker(UserMapper.toBookerDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public BookingOwnerDto toBookingOwnerDto (Booking booking){
        return BookingOwnerDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
