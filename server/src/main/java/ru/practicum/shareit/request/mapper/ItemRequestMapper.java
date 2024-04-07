package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest toRequest(ItemPostDto itemPostDto) {
        return ItemRequest.builder()
                .description(itemPostDto.getDescription())
                .build();
    }

    public ItemRequestDtoOut toItemRequestDtoOut(ItemRequest itemRequest) {
        List<ItemDto> itemDtos = new ArrayList<>();

        if (!Objects.isNull(itemRequest.getItems())) {
            itemDtos = itemRequest.getItems().stream()
                    .map(ItemMapper::toItemDTO)
                    .collect(Collectors.toList());
        }

        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemDtos)
                .build();
    }
}
