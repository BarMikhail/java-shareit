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
    @Email
    @NotBlank
    private String email;
}