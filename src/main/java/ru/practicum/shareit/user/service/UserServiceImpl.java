package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        return UserMapper.toUserDTO(repository.getUserById(id));
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
