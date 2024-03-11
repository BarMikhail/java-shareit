package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {

    private User owner = User.builder()
            .id(1L)
            .name("Test")
            .email("test@ya.ru")
            .build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Test !test test")
            .build();

    private Item item =Item.builder()
            .id(1L)
            .available(true)
            .description("test test")
            .owner(owner)
            .itemRequest(itemRequest)
            .name("test")
            .build();

    @Test
    void toItemDtoTest() {


        ItemDto itemDto = ItemMapper.toItemDTO(item);


        assertNotNull(itemDto);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(item.getItemRequest().getId(), itemDto.getRequestId());


    }

    @Test
    void toItemDtoBookingTest() {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("Test !test test")
                .authorName("Appa")
                .created(LocalDateTime.now())
                .build();

        List<CommentDto> comments = new ArrayList<>(List.of(comment));

        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item, comments);

        assertNotNull(itemDtoBooking);

        assertEquals(item.getId(), itemDtoBooking.getId());
        assertEquals(item.getName(), itemDtoBooking.getName());
        assertEquals(item.getDescription(), itemDtoBooking.getDescription());
        assertEquals(item.getAvailable(), itemDtoBooking.getAvailable());
        assertEquals(comments, itemDtoBooking.getComments());
    }

    @Test
    void toItemRequestTest() {

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .name("Test")
                .description("test test")
                .available(true)
                .build();

        Item toItemRequest = ItemMapper.toItemRequest(itemRequestDto, owner);

        assertNotNull(toItemRequest);

        assertEquals(itemRequestDto.getName(), toItemRequest.getName());
        assertEquals(itemRequestDto.getDescription(), toItemRequest.getDescription());
        assertEquals(itemRequestDto.getAvailable(), toItemRequest.getAvailable());
        assertEquals(owner, toItemRequest.getOwner());
    }

}
