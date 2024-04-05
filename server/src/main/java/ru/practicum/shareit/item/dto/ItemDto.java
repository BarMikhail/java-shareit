package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    private String description;
    private String name;
    private Boolean available;

    private Long owner;

    private Long requestId;
}
