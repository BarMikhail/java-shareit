package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDTO(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owen(item.getOwner())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto item) {
        return Item.builder()
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwen())
                .available(item.getAvailable())
                .build();
    }
}
