package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private int id;
    private User owner;
    private String description;
    private String name;
    private boolean availability;

}
