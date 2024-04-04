package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

//import javax.validation.Valid;
//import javax.validation.constraints.Positive;
//import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDtoResponse createBookingRequest(@RequestHeader(X_SHARER_USER_ID) Long userId,
//                                                   @Valid
                                                   @RequestBody BookingDto bookingDto) {
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
                                                     @RequestParam(defaultValue = "ALL") BookingState state,
//                                                     @PositiveOrZero
                                                         @RequestParam(defaultValue = "0") Integer from,
//                                                     @Positive
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение списка всех бронирований");
        return service.getAllBooking(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllByOwner(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                  @RequestParam(defaultValue = "ALL") BookingState state,
//                                                  @PositiveOrZero
                                                      @RequestParam(defaultValue = "0") Integer from,
//                                                  @Positive
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("получить список всех бронирований собственника вещей");
        return service.getAllBookingByOwner(state, userId, from, size);
    }
}
