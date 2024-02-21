package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @Email(groups = {Create.class, Update.class}, message = "Где почта?!")
    @NotBlank(groups = {Create.class}, message = "Что-то пошло не так")
    private String email;
}