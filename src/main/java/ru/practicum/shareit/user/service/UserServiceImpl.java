package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserDto userDto = UserMapper.toUserDTO(user);
            userDtos.add(userDto);
        }

        return userDtos;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new NotFoundException("Нет такого пользователя"));
        return UserMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto user) {
        return UserMapper.toUserDTO(repository.save(UserMapper.toUser(user)));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User updateUser = repository.findById(id).orElseThrow(() -> new NotFoundException("Нет такого пользователя"));
        User user = UserMapper.toUser(userDto);
        if (user.getName() != null && !user.getName().isBlank()) {
            updateUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            updateUser.setEmail(user.getEmail());
        }
        return UserMapper.toUserDTO(repository.save(updateUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
