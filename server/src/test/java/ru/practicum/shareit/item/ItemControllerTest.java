package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;

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

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;
    private CommentRequestDto commentRequestDto;
    private ItemDto firstItemDto;
    private ItemDto secondItemDto;
    private ItemDtoBooking firstItemDtoBooking;
    private ItemDtoBooking secondItemDtoBooking;
    private CommentDto commentDto;

    @BeforeEach
    void beforeEach() {
        commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .authorName("111")
                .text(commentRequestDto.getText())
                .created(LocalDateTime.now())
                .build();


        firstItemDto = ItemDto.builder()
                .id(1L)
                .name("Test")
                .description("test test")
                .available(true)
                .requestId(1L)
                .owner(1L)
                .build();

        secondItemDto = ItemDto.builder()
                .id(2L)
                .name("Test1")
                .description("test1 test")
                .available(true)
                .requestId(1L)
                .owner(1L)
                .build();

        firstItemDtoBooking = ItemDtoBooking.builder()
                .id(1L)
                .name("d")
                .description("dfw")
                .available(true)
                .comments(List.of(commentDto))
                .build();
        secondItemDtoBooking = ItemDtoBooking.builder()
                .id(2L)
                .name("d1")
                .description("dfw1")
                .available(true)
                .comments(List.of(commentDto))
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .name("Test")
                .available(true)
                .description("test test")
                .build();
    }


    @Test
    void addItemTest() throws Exception {
        when(itemService.addItem(anyLong(), any(ItemRequestDto.class))).thenReturn(firstItemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(firstItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(firstItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(firstItemDto.getRequestId()), Long.class));

        verify(itemService, times(1)).addItem(1L, itemRequestDto);
    }

    @Test
    void addItem_InvalidDto_ReturnsBadRequest() throws Exception {
        ItemRequestDto invalidDto = ItemRequestDto.builder().build();

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.updateItem(anyLong(), any(ItemDto.class), anyLong())).thenReturn(firstItemDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(mapper.writeValueAsString(firstItemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(firstItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(firstItemDto.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(firstItemDto.getRequestId()), Long.class));

        verify(itemService, times(1)).updateItem(1L, firstItemDto, 1L);
    }

    @Test
    void updateItem_InvalidDto_ReturnsBadRequest() throws Exception {
        ItemDto invalidDto = ItemDto.builder()
                .description("012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910." +
                        "012345678910.")
                .build();

        mvc.perform(patch("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDto))
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(firstItemDtoBooking);

        mvc.perform(get("/items/{itemId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstItemDtoBooking.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstItemDtoBooking.getName()), String.class))
                .andExpect(jsonPath("$.description", is(firstItemDtoBooking.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(firstItemDtoBooking.getAvailable()), Boolean.class));


        verify(itemService, times(1)).getItemById(1L, 1L);
    }

    @Test
    void getAllItemByUserTest() throws Exception {
        when(itemService.getAllItemByOwnerId(anyLong(), anyInt(), anyInt())).thenReturn(List.of(firstItemDtoBooking, secondItemDtoBooking));

        mvc.perform(get("/items")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemDtoBooking, secondItemDtoBooking))));

        verify(itemService, times(1)).getAllItemByOwnerId(1L, 0, 10);
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems(anyString(), anyInt(), anyInt())).thenReturn(List.of(firstItemDto, secondItemDto));

        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstItemDto, secondItemDto))));

        verify(itemService, times(1)).searchItems("test", 0, 10);
    }

    @Test
    void createComment() throws Exception {
        when(itemService.createComment(any(CommentRequestDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(mapper.writeValueAsString(commentRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));

        verify(itemService, times(1)).createComment(commentRequestDto, 1L, 1L);
    }

    @Test
    void createComment_InvalidDto_ReturnsBadRequest() throws Exception {
        CommentRequestDto invalidDto = CommentRequestDto.builder().build();

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(mapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}