package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private BookerDto bookerDto;
    private ItemDto itemDto;
    private BookingDto bookingDto;
    private BookingDtoResponse firstBookingDto;
    private BookingDtoResponse secondBookingDto;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";


    @BeforeEach
    void beforeEach() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        bookerDto = BookerDto.builder()
                .id(1L)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Test")
                .description("test test")
                .available(true)
                .requestId(1L)
                .owner(1L)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        firstBookingDto = BookingDtoResponse.builder()
                .id(1L)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .booker(bookerDto)
                .item(itemDto)
                .status(BookingStatus.WAITING)
                .build();

        secondBookingDto = BookingDtoResponse.builder()
                .id(2L)
                .start(LocalDateTime.of(2024, 3, 11, 8, 0))
                .end(LocalDateTime.of(2024, 3, 11, 12, 0))
                .booker(bookerDto)
                .item(itemDto)
                .status(BookingStatus.WAITING)
                .build();

    }


    @Test
    void createBookingRequest() throws Exception {
        when(bookingService.createBookingRequest(any(BookingDto.class), anyLong())).thenReturn(firstBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(firstBookingDto.getStatus().toString()), BookingStatus.class))
                .andExpect(jsonPath("$.booker.id", is(firstBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(firstBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).createBookingRequest(bookingDto, 1L);
    }

    @Test
    void changeBookingStatus() {
    }

    @Test
    void getBookingDetails() {
    }

    @Test
    void getAllByBookings() {
    }

    @Test
    void getAllByOwner() {
    }
}