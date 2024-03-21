package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {


    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDtoOut firstItemRequestDto;

    private ItemRequestDtoOut secondItemRequestDto;

    private ItemPostDto itemPostDto;

    @BeforeEach
    void beforeEach() {

        firstItemRequestDto = ItemRequestDtoOut.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(LocalDateTime.now())
                .build();

        secondItemRequestDto = ItemRequestDtoOut.builder()
                .id(2L)
                .description("ItemRequest 2")
                .created(LocalDateTime.now())
                .build();

        itemPostDto = ItemPostDto.builder()
                .description("ItemRequest 1")
                .build();
    }

    @Test
    void addItemRequestTest() throws Exception {
        when(itemRequestService.addItemRequest(anyLong(), any(ItemPostDto.class))).thenReturn(firstItemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(firstItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(firstItemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1)).addItemRequest(1L, itemPostDto);
    }

    @Test
    void addItemRequest_InvalidDto_ReturnsBadRequest() throws Exception {
        ItemPostDto invalidDto = ItemPostDto.builder().build();

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRequestsTest() throws Exception {
        when(itemRequestService.getItemRequests(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(firstItemRequestDto, secondItemRequestDto));

        mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemRequestDto, secondItemRequestDto))));

        verify(itemRequestService, times(1)).getItemRequests(0, 10, 1L);
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(firstItemRequestDto);

        mvc.perform(get("/requests/{requestId}", firstItemRequestDto.getId())
                        .content(mapper.writeValueAsString(firstItemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(firstItemRequestDto.getDescription()), String.class));

        verify(itemRequestService, times(1)).getItemRequestById(1L, 1L);
    }

    @Test
    void getItemRequestsTest() throws Exception {
        when(itemRequestService.getAllItemRequestsByUserId(anyLong()))
                .thenReturn(List.of(firstItemRequestDto, secondItemRequestDto));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemRequestDto, secondItemRequestDto))));

        verify(itemRequestService, times(1)).getAllItemRequestsByUserId(1L);
    }
}