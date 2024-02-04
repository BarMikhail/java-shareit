package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Item item);

    Optional<Item> getItemById(long itemId);

    void updateItem(Item item, long itemId);

    List<Item> findByUserId(long userId);

    List<Item> findBySearch(String searchText);
}
