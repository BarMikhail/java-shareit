package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExistEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        return userMap.get(id);
    }

    @Override
    public User createUser(User user) {
        for (User userCheck : getAllUsers()) {
            if (userCheck.getEmail().equals(user.getEmail()) && userCheck.getId() != id) {
                throw new ExistEmailException("Почта уже существует");
            }
        }
        user.setId(generateUserId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        User newUser = getUserById(id);
        if (user.getName() != null) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            for (User userCheck : getAllUsers()) {
                if (userCheck.getEmail().equals(user.getEmail()) && userCheck.getId() != id) {
                    throw new ExistEmailException("Почта уже существует");
                }
            }
            newUser.setEmail(user.getEmail());
        }

        return newUser;
    }

    @Override
    public void deleteUser(long id) {
        userMap.remove(id);
    }

    private long generateUserId() {
        return id++;
    }
}
