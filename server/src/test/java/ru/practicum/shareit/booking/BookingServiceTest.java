package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @InjectMocks
    private BookingServiceImp bookingService;


    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Item item;
    private Booking booking;
    private ItemDto itemDto;
    private BookingDto bookingDto;
    private Integer from;
    private Integer size;


    @BeforeEach
    void beforeEach() {
        from = 0;
        size = 10;

        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .available(true)
                .owner(user)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .available(true)
                .owner(1L)
                .build();

        booking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(item)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    void createBookingRequestTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(BookingMapper.toBooking(bookingDto, item, user));

        BookingDtoResponse bookingDtoResponse = bookingService.createBookingRequest(bookingDto, 2L);

        assertNotNull(bookingDtoResponse);
        assertEquals(bookingDtoResponse.getItem().getId(), item.getId());
        assertEquals(bookingDtoResponse.getBooker().getId(), user.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOwnerTriesToBookOwnItemTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBookingRequest(bookingDto, 1L));
        assertEquals(exception.getMessage(), "Зачем владельцу бронировать свой товар?");
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenBookingDatesAreInvalidTest() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> bookingService.createBookingRequest(bookingDto, 1L));
        assertEquals(exception.getMessage(), "Дата пошла погулять");
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenItemIsNotAvailableTest() {
        item.setAvailable(false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> bookingService.createBookingRequest(bookingDto, 1L));
        assertEquals(exception.getMessage(), "Товар занят");
    }

    @Test
    void updateBookingStatusByOwnerTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse result = bookingService.updateBookingStatusByOwner(1L, 1L, true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void shouldUpdateBookingStatusByOwnerTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse result = bookingService.updateBookingStatusByOwner(1L, 1L, false);

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenBookingAlreadyApprovedTest() {
        booking.setStatus(BookingStatus.APPROVED);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> bookingService.updateBookingStatusByOwner(1L, 1L, true));
        assertEquals(exception.getMessage(), "Бронирование вещи уже одобрено");
    }

    @Test
    void shouldThrowInvalidDataExceptionWhenBookingRejectedTest() {
        booking.setStatus(BookingStatus.REJECTED);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        InvalidDataException exception = assertThrows(InvalidDataException.class,
                () -> bookingService.updateBookingStatusByOwner(1L, 1L, false));
        assertEquals(exception.getMessage(), "Бронирование вещи отклонено");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOwnerCannotUpdateBookingStatusTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatusByOwner(1L, 2L, true));
        assertEquals(exception.getMessage(), "Только владелец может подтвердить бронирование предмета");
    }

    @Test
    void getBookingDetailsTest() {
        BookingDtoResponse b = BookingMapper.toBookingResponse(booking, ItemMapper.toItemDTO(item));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        BookingDtoResponse result = bookingService.getBookingDetails(1L, 1L);

        assertEquals(b, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserCannotGetBookingDetailsTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));


        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getBookingDetails(1L, 2L));
        assertEquals(exception.getMessage(), "Только автор или владелец может проверить детали бронирования");
    }

    @Test
    void shouldReturnAllBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.ALL, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnCurrentBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.CURRENT, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnPastBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerIdAndEndDateBefore(anyLong(),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.PAST, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }


    @Test
    void shouldReturnFutureBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerIdAndStartDateAfter(anyLong(),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.FUTURE, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnWaitingBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.WAITING, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnRejectedBookingsForUserTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBooking(BookingState.REJECTED, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnAllBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class))).thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.ALL, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnCurrentBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.CURRENT, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnPastBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndEndDateBefore(anyLong(),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.PAST, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnFutureBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStartDateAfter(anyLong(),
                any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.FUTURE, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnWaitingBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(),
                any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.WAITING, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldReturnRejectedBookingsForOwnerTest() {
        List<BookingDtoResponse> expectedBookingsDtoOut = List.of(BookingMapper.toBookingResponse(booking, itemDto));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findAllByItemOwnerId(anyLong(), any(Sort.class)))
                .thenReturn(List.of(booking));
        when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(),
                any(BookingStatus.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoResponse> result = bookingService.getAllBookingByOwner(BookingState.REJECTED, 1L, from, size);

        assertEquals(expectedBookingsDtoOut, result);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenOwnerHasNoItemsTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.getAllBookingByOwner(BookingState.ALL, 2L, from, size));
        assertEquals(exception.getMessage(), "У этого пользователя нет добавленного товара");
    }

}