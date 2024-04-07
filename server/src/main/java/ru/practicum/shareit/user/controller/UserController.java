package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("Создание пользователя");
        log.info("Посмотрим пользователь создан {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody UserDto userDto) {
        log.info("Обновление пользователя id = {}, что меняется {}", userId, userDto);
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        log.info("Вывод всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Вывод определенного пользователя, id = {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") long userId) {
        log.info("Удалеине определенного пользователя, id = {}", userId);
        userService.deleteUser(userId);
    }
}
