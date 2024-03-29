package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long userId, ItemRequestDto itemRequestDto);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    ItemDtoBooking getItemById(Long itemId, Long userId);

    List<ItemDtoBooking> getAllItemByOwnerId(Long userId, Integer from, Integer size);

    List<ItemDto> searchItems(String searchText, Integer from, Integer size);

    CommentDto createComment(CommentRequestDto commentRequestDto, Long userId, Long itemId);
}
