package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long itemId, ItemDto itemDto, long userId);

    ItemDto getItemById(long itemId);

    List<ItemDto> getAllItem(long userId);

    List<ItemDto> searchItems(String searchText);
}
