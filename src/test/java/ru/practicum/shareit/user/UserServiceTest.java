package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    private User firstUser;
    private User seconfUser;
    private UserDto firstUserDto;
    private UserDto secondUserDto;

    @BeforeEach
    void beforeEach() {
        firstUser = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();
        firstUserDto = UserMapper.toUserDTO(firstUser);

        seconfUser = User.builder()
                .id(2L)
                .name("Proba")
                .email("proba@ya.ru")
                .build();

        secondUserDto = UserMapper.toUserDTO(seconfUser);
    }


    @Test
    void getAllUsers() {

        when(userRepository.findAll()).thenReturn(List.of(firstUser, seconfUser));

        List<UserDto> userList = userService.getAllUsers();

        assertEquals(userList.size(), 2);
        assertEquals(userList.get(0).getId(), firstUser.getId());
        assertEquals(userList.get(0).getEmail(), firstUser.getEmail());
        assertEquals(userList.get(1).getId(), seconfUser.getId());
        assertEquals(userList.get(1).getName(), seconfUser.getName());
    }

    @Test
    void getUserById() {
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(seconfUser));

        UserDto resultUser = userService.getUserById(2L);

        assertEquals(resultUser.getId(), seconfUser.getId());
        assertEquals(resultUser.getName(), seconfUser.getName());
        assertEquals(resultUser.getEmail(), seconfUser.getEmail());
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(firstUser);

        UserDto resultUser = userService.createUser(firstUserDto);

        assertEquals(resultUser.getId(), firstUser.getId());
        assertEquals(resultUser.getName(), firstUser.getName());
        assertEquals(resultUser.getEmail(), firstUser.getEmail());
    }

    @Test
    void updateUser() {

        when(userRepository.save(any(User.class))).thenReturn(firstUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstUser));

        UserDto resultUser = userService.updateUser(firstUserDto.getId(), firstUserDto);

        assertEquals(firstUserDto, resultUser);
        assertEquals(firstUserDto.getName(), resultUser.getName());
        assertEquals(firstUserDto.getEmail(), resultUser.getEmail());
    }

    @Test
    void deleteUser() {
        userService.deleteUser(firstUser.getId());

        verify(userRepository).deleteById(firstUser.getId());
    }
}