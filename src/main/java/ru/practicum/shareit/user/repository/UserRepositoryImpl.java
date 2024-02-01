package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.CustomExceptionHandler;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private long id = 1;
    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(long id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new CustomExceptionHandler("Пользователь не найден");
        }
    }

    @Override
    public User createUser(User user) {
        for (User userCheck : getAllUsers()) {
            if (userCheck.getEmail().equals(user.getEmail())) {
                throw new CustomExceptionHandler("Почта уже существует");
            }
        }
        user.setId(generateUserId());
        userMap.put(id, user);
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        User newUser = userMap.get(id);
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            for (User userCheck : getAllUsers()) {
                if (userCheck.getEmail().equals(user.getEmail()) && userCheck.getId() != id) {
                    throw new CustomExceptionHandler("Почта уже существует");
                }
            }
            newUser.setEmail(user.getEmail());
        }
        userMap.put(id, newUser);
        return userMap.get(user.getId());
    }

    @Override
    public void deleteUser(long id) {
        userMap.remove(id);
    }

    private long generateUserId() {
        return id++;
    }
}
