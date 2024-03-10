package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemRequestMapperTest {

    @Test
    void toRequest() {
        ItemPostDto itemPostDto = ItemPostDto.builder()
                .description("Test")
                .build();

        ItemRequest itemRequest = ItemRequestMapper.toRequest(itemPostDto);

        assertNotNull(itemRequest);

        assertEquals(itemRequest.getDescription(), itemPostDto.getDescription());

    }

    @Test
    void toItemRequestDtoOut() {
        LocalDateTime created = LocalDateTime.of(2024, 3, 10, 0, 0);

        User user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("guitar")
                .description("a very good tool")
                .available(true)
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("ItemRequest 1")
                .created(created)
                .items(List.of(item))
                .user(user)
                .build();

        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toItemRequestDtoOut(itemRequest);
        ItemDto itemDto = ItemMapper.toItemDTO(item);

        assertEquals(itemRequestDtoOut.getId(), itemRequest.getId());
        assertEquals(itemRequestDtoOut.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDtoOut.getCreated(), itemRequest.getCreated());
        assertEquals(itemRequestDtoOut.getItems(), List.of(itemDto));


    }

}