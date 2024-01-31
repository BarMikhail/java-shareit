package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toDTO(User user){
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
