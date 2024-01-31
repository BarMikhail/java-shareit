package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    private List<User> userList;

    public UserRepositoryImpl() {
        this.userList = new ArrayList<>();
    }

    @Override
    public List<User> getAllUsers() {
        return userList;
    }

    @Override
    public User getUserById(long id) {
        for (User user : userList) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User createUser(User user) {
        long userId = generateUserId();
        user.setId(userId);

        // Сохранение пользователя в памяти
        userList.add(user);

        return user;
    }

    @Override
    public User updateUser(long id, User updatedUser) {
        for (User user : userList) {
            if (user.getId() == id) {
                // Обновление данных пользователя
                user.setName(updatedUser.getName());
                user.setEmail(updatedUser.getEmail());
                return user;
            }
        }
        return null;
    }

    @Override
    public void deleteUser(long id) {
        userList.removeIf(user -> user.getId() == id);
    }

    private long generateUserId() {
        return userList.size() + 1;
    }
}
