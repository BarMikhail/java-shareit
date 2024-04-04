package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotBlank(groups = {Create.class})
    @Size(max = 100, groups = {Create.class, Update.class})
    private String description;
    @NotBlank(groups = {Create.class})
    @Size(max = 200, groups = {Create.class, Update.class})
    private String name;
    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long owner;

    private Long requestId;
}
