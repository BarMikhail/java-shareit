package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ExistEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User createUser(User user) {
        check(user, id);
        user.setId(generateUserId());
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        User newUser = userMap.get(id);
        if (user.getName() != null && !user.getName().isBlank()) {
            newUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            check(user, id);
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

    private void check(User user, long id) {
        for (User userCheck : getAllUsers()) {
            if (userCheck.getEmail().equals(user.getEmail())
                    && userCheck.getId() != id) {
                throw new ExistEmailException("Почта уже существует");
            }
        }
    }
}
