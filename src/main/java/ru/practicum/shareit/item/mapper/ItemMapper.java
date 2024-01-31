package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDTO(ItemDto itemDto){
        return ItemDto.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .availability(itemDto.isAvailability())
                .build();
    }

    public static Item toItem(Item item){
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .availability(item.isAvailability())
                .build();
    }
}
