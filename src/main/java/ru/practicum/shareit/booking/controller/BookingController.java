package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoResponse createBookingRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                   @Valid @RequestBody BookingDto bookingDto) {
        log.info("Создание запроса на бронирование {}, юзером {}", bookingDto, userId);
        return service.createBookingRequest(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse changeBookingStatus(@PathVariable("bookingId") Long bookingId,
                                                  @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam boolean approved) {
        log.info("Обновление статуса бронирования {} владельцем {}", bookingId, userId);
        return service.updateBookingStatusByOwner(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingDetails(@PathVariable("bookingId") Long bookingId,
                                                @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Ответ на запрос о бронировании {}", bookingId);
        return service.getBookingDetails(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllByBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                     @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Получение списка всех бронирований");
        return service.getAllBooking(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("получить список всех бронирований собственника вещей");
        return service.getAllBookingByOwner(state, userId);
    }
}
