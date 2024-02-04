package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> dataItem = new HashMap<>();
    private final Map<Long, List<Item>> itemsByUser = new HashMap<>();
    private long id = 1;

    @Override
    public Item addItem(Item item) {
        item.setId(generateItemId());
        dataItem.put(item.getId(), item);

        long userId = item.getOwner().getId();
        itemsByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(item);
        return item;
    }

    @Override
    public void updateItem(Item item, long itemId) {
        dataItem.put(itemId, item);
        long userId = item.getOwner().getId();
        itemsByUser.get(userId).remove(item);
        itemsByUser.get(userId).add(item);
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(dataItem.get(itemId));
    }

    @Override
    public List<Item> findByUserId(long userId) {
        return itemsByUser.get(userId);
    }

    @Override
    public List<Item> findBySearch(String searchText) {
        List<Item> matchingItems = new ArrayList<>();
        String lowerCaseSearchText = searchText.toLowerCase();

        for (Item item : dataItem.values()) {
            if (item.getAvailable().equals(true) && (item.getDescription().toLowerCase().contains(lowerCaseSearchText)
                    || item.getName().toLowerCase().contains(lowerCaseSearchText))) {
                matchingItems.add(item);
            }
        }

        return matchingItems;
    }

    private long generateItemId() {
        return id++;
    }

}
