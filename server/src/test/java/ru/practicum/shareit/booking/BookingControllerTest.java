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
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.BookerDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

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
    void createBookingRequestTest() throws Exception {
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
    void createBookingRequest_InvalidDto_ReturnsBadRequest() throws Exception {
        BookingDto invalidDto = BookingDto.builder().build();

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void changeBookingStatusTest() throws Exception {
        when(bookingService.updateBookingStatusByOwner(anyLong(), anyLong(), anyBoolean())).thenReturn(firstBookingDto);

        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(firstBookingDto.getStatus().toString()), BookingStatus.class))
                .andExpect(jsonPath("$.booker.id", is(firstBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(firstBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).updateBookingStatusByOwner(1L, 1L, true);
    }

    @Test
    void getBookingDetailsTest() throws Exception {
        when(bookingService.getBookingDetails(anyLong(), anyLong())).thenReturn(firstBookingDto);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstBookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(firstBookingDto.getStatus().toString()), BookingStatus.class))
                .andExpect(jsonPath("$.booker.id", is(firstBookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(firstBookingDto.getItem().getId()), Long.class));

        verify(bookingService, times(1)).getBookingDetails(1L, 1L);
    }

    @Test
    void getAllByBookingsTest() throws Exception {
        when(bookingService.getAllBooking(any(BookingState.class), anyLong(), anyInt(), anyInt())).thenReturn(List.of(firstBookingDto, secondBookingDto));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstBookingDto, secondBookingDto))));

        verify(bookingService, times(1)).getAllBooking(BookingState.ALL, 1L, 0, 10);
    }

    @Test
    void getAllByOwnerTest() throws Exception {
        when(bookingService.getAllBookingByOwner(any(BookingState.class), anyLong(), anyInt(), anyInt())).thenReturn(List.of(firstBookingDto, secondBookingDto));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstBookingDto, secondBookingDto))));

        verify(bookingService, times(1)).getAllBookingByOwner(BookingState.ALL, 1L, 0, 10);
    }
}