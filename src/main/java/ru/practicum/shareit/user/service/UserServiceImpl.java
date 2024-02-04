package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.getAllUsers();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserMapper.toUserDTO(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    public UserDto getUserById(long id) {
        User user = repository.getUserById(id).orElseThrow(() -> new NotFoundException("Нет такого пользователя"));
        return UserMapper.toUserDTO(user);
    }

    @Override
    public UserDto createUser(UserDto user) {
        return UserMapper.toUserDTO(repository.createUser(UserMapper.toUser(user)));
    }

    @Override
    public UserDto updateUser(long id, UserDto user) {
        return UserMapper.toUserDTO(repository.updateUser(id, UserMapper.toUser(user)));
    }

    @Override
    public void deleteUser(long id) {
        repository.deleteUser(id);
    }
}
