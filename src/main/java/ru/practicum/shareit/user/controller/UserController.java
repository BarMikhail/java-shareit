package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Создание пользователя");
        return userService.createUser(userDto);
    }

    @PutMapping("/userId")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Обновление пользователя");
        return userService.updateUser(userId, userDto);
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        log.info("Вывод всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/userId")
    public UserDto getUserById(@PathVariable long userId) {
        log.info("Вывод определенного пользователя");
        return userService.getUserById(userId);
    }

    @DeleteMapping("/userId")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удалеине определенного пользователя");
        userService.deleteUser(userId);
    }
}
