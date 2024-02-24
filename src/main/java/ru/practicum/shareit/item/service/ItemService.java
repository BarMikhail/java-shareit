package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    ItemDtoBooking getItemById(Long itemId, Long userId);

    List<ItemDtoBooking> getAllItemByOwnerId(Long userId);

    List<ItemDto> searchItems(String searchText);

    CommentDto createComment(CommentDto commentDto, Long userId, Long itemId);
}
