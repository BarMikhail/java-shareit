package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item getItemById(long itemId);

    void updateItem(Item item, long itemId);

    List<Item> findByUserId(long userId);

    List<Item> findBySearch(String searchText);
}
