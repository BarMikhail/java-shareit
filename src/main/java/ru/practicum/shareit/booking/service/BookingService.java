package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    BookingDtoResponse createBookingRequest(BookingDto bookingDto, Long userId);

    BookingDtoResponse updateBookingStatusByOwner(Long bookingID, Long userId, boolean approved);

    BookingDtoResponse getBookingDetails(Long bookingId, Long userId);

    List<BookingDtoResponse> getAllBooking(BookingState state, Long userId);

    List<BookingDtoResponse> getAllBookingByOwner(BookingState state, Long userId);
}
