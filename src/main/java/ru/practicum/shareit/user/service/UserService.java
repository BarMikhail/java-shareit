package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long id);

    User createUser(User user);

    User updateUser(long id, User updatedUser);

    void deleteUser(long id);
}
