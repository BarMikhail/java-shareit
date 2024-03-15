package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImp;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
//
//        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
//        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(item));
//        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
//                .thenReturn(BookingMapper.toBooking(bookingDto,item,user));
//
//        BookingDtoResponse bookingDtoResponse = bookingService.createBookingRequest(bookingDto,2L);
//
//        assertNotNull(bookingDtoResponse);
////        assertEquals(bookingDtoResponse.getItem(),item);
////        assertEquals(bookingDtoResponse.getBooker(),user);
//
//        Mockito.verify(bookingRepository, Mockito.times(1)).save(Mockito.any(Booking.class));

    }

    @Test
    void updateBookingStatusByOwner() {
    }

    @Test
    void getBookingDetails() {
    }

    @Test
    void getAllBooking() {
    }

    @Test
    void getAllBookingByOwner() {
    }
}