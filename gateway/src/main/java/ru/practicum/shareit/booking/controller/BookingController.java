package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBookingRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                       @Valid @RequestBody BookingDto bookingDto) {
        log.info("Создание запроса на бронирование {}, юзером {}", bookingDto, userId);
        return bookingClient.createBookingRequest(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@PathVariable("bookingId") Long bookingId,
                                                      @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                      @RequestParam boolean approved) {
        log.info("Обновление статуса бронирования {} владельцем {}", bookingId, userId);
        return bookingClient.updateBookingStatusByOwner(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingDetails(@PathVariable("bookingId") Long bookingId,
                                                    @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Ответ на запрос о бронировании {}", bookingId);
        return bookingClient.getBookingDetails(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookings(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка всех бронирований");
        return bookingClient.getAllBooking(state, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @RequestParam(defaultValue = "ALL") BookingState state,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("получить список всех бронирований собственника вещей");
        return bookingClient.getAllBookingByOwner(state, userId, from, size);
    }
}
