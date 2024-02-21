package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long owner;

    private ItemRequest itemRequest;
}
