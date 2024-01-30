package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private int id;
    private  String owner;
    private  String description;
    private  String name;
    private Boolean status;

}
