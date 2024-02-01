package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDto {
    private int id;
    private String description;
    private String name;
    private boolean availability;

}
