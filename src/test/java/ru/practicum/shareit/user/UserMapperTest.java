package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.BookerDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperTest {
    @Test
    void toUser_FromUserDto() {

        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Test")
                .email("test@ya.ru")
                .build();

        User user = UserMapper.toUser(userDto);

        assertNotNull(user);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void toUserDto_FromUserDto() {

        User user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        UserDto userDto = UserMapper.toUserDTO(user);

        assertNotNull(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }


    @Test
    void toBookerDto_FromUserDto() {

        User user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        BookerDto userDto = UserMapper.toBookerDto(user);

        assertNotNull(userDto);

        assertEquals(userDto.getId(), user.getId());
    }
}