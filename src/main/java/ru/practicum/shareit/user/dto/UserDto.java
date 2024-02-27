package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(groups = {Create.class})
    @Size(max = 100, groups = {Create.class, Update.class})
    private String name;

    @Email(groups = {Create.class, Update.class}, message = "Где почта?!")
    @NotBlank(groups = {Create.class}, message = "Что-то пошло не так")
    @Size(max = 100, groups = {Create.class, Update.class})
    private String email;
}