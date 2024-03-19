package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
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
    private User user2;
    private Item item;
    private Booking booking;
    private ItemDto itemDto;
    private BookingDto bookingDto;


    @BeforeEach
    void beforeEach() {

        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        user2 = User.builder()
                .id(2L)
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
                .requestId(1L)
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
    void createBookingRequest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(BookingMapper.toBooking(bookingDto,item,user));

        BookingDtoResponse bookingDtoResponse = bookingService.createBookingRequest(bookingDto,2L);

        assertNotNull(bookingDtoResponse);
        assertEquals(bookingDtoResponse.getItem().getId(),item.getId());
        assertEquals(bookingDtoResponse.getBooker().getId(),user.getId());

        verify(bookingRepository, times(1)).save(any(Booking.class));

    }

    @Test
    void updateBookingStatusByOwner() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse result = bookingService.updateBookingStatusByOwner(1L,1L,true);

        assertEquals(BookingStatus.APPROVED,result.getStatus());

        verify(bookingRepository,times(1)).save(any(Booking.class));
    }

    @Test
    void getBookingDetails() {
        BookingDtoResponse b = BookingMapper.toBookingResponse(booking, ItemMapper.toItemDTO(item));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        BookingDtoResponse result = bookingService.getBookingDetails(1L,1L);

        assertEquals(b,result);

        verify(bookingRepository,times(1)).findById(anyLong());

    }

    @Test
    void getAllBooking() {
    }

    @Test
    void getAllBookingByOwner() {
    }

    @Test
    void getAllBooking_ShouldReturnListOfBookingDtoResponse_ForAllStates() {
        // Arrange
//        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "startDate"));

        // Setting up mock behavior for each state
//        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        // Case: ALL
        List<Booking> allBookings = Collections.singletonList(booking);
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(allBookings);

        // Case: CURRENT
        List<Booking> currentBookings = Collections.singletonList(booking);
        when(bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(
                anyLong(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                any(Pageable.class)))
                .thenReturn(currentBookings);
//
//        // Case: PAST
//        List<Booking> pastBookings = Collections.singletonList(booking);
//        when(bookingRepository.findAllByBookerIdAndEndDateBefore(anyLong(), LocalDateTime.now(), any(Pageable.class)))
//                .thenReturn(pastBookings);
//
//        // Case: FUTURE
//        List<Booking> futureBookings = Collections.singletonList(booking);
//        when(bookingRepository.findAllByBookerIdAndStartDateAfter(anyLong(), LocalDateTime.now(), any(Pageable.class)))
//                .thenReturn(futureBookings);
//
//        // Case: WAITING
//        List<Booking> waitingBookings = Collections.singletonList(booking);
//        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), BookingStatus.WAITING, any(Pageable.class)))
//                .thenReturn(waitingBookings);
//
//        // Case: REJECTED
//        List<Booking> rejectedBookings = Collections.singletonList(booking);
//        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), BookingStatus.REJECTED, any(Pageable.class)))
//                .thenReturn(rejectedBookings);

        // Act and Assert for each state

        // Case: ALL
        List<BookingDtoResponse> allBookingsResponse = bookingService.getAllBooking(BookingState.ALL, user.getId(), from, size);
        assertNotNull(allBookingsResponse);
        assertEquals(allBookings.size(), allBookingsResponse.size());
        // Additional assertions for allBookingsResponse
//
        // Case: CURRENT
        List<BookingDtoResponse> currentBookingsResponse = bookingService.getAllBooking(BookingState.CURRENT, user.getId(), from, size);
        assertNotNull(currentBookingsResponse);
        assertEquals(currentBookings.size(), currentBookingsResponse.size());
        // Additional assertions for currentBookingsResponse
//
//        // Case: PAST
//        List<BookingDtoResponse> pastBookingsResponse = bookingService.getAllBooking(BookingState.PAST, user.getId(), from, size);
//        assertNotNull(pastBookingsResponse);
//        assertEquals(pastBookings.size(), pastBookingsResponse.size());
//        // Additional assertions for pastBookingsResponse
//
//        // Case: FUTURE
//        List<BookingDtoResponse> futureBookingsResponse = bookingService.getAllBooking(BookingState.FUTURE, user.getId(), from, size);
//        assertNotNull(futureBookingsResponse);
//        assertEquals(futureBookings.size(), futureBookingsResponse.size());
//        // Additional assertions for futureBookingsResponse
//
//        // Case: WAITING
//        List<BookingDtoResponse> waitingBookingsResponse = bookingService.getAllBooking(BookingState.WAITING, user.getId(), from, size);
//        assertNotNull(waitingBookingsResponse);
//
//        assertEquals(waitingBookings.size(), waitingBookingsResponse.size());
//        // Additional assertions for waitingBookingsResponse
//
//        // Case: REJECTED
//        List<BookingDtoResponse> rejectedBookingsResponse = bookingService.getAllBooking(BookingState.REJECTED, user.getId(), from, size);
//        assertNotNull(rejectedBookingsResponse);
//        assertEquals(rejectedBookings.size(), rejectedBookingsResponse.size());
//        // Additional assertions for rejectedBookingsResponse
    }
}