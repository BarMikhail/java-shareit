package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Builder
@Data
public class ItemDto {
    private long id;
    @NotBlank
    private String description;
    @NotBlank
    private String name;
    @NotNull
    private Boolean available;
    private User owen;

}
