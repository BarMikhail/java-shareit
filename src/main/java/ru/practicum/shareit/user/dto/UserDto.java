package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class UserDto {
    private long id;
    @NotBlank
    private String name;

    @Email(message = "Где почта?!")
    @NotBlank(message = "Что-то пошло не так")

    private String email;
}