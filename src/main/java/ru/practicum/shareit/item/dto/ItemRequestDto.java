package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.additionally.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @NotBlank(groups = {Create.class})
    @Size(max = 100, groups = {Create.class})
    private String description;
    @NotBlank(groups = {Create.class})
    @Size(max = 200, groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    private Boolean available;
}
