package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserDto toUserDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public BookerDto toBookerDto(User user) {
        return BookerDto.builder()
                .id(user.getId())
                .build();
    }
}
