package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private UserDto firstUserDto;
    private UserDto secondUserDto;

    @BeforeEach
    void beforeEach() {
        firstUserDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();
        secondUserDto = UserDto.builder()
                .id(2L)
                .name("Proba")
                .email("proba@ya.ru")
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(firstUserDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).createUser(firstUserDto);
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(firstUserDto);

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(firstUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).updateUser(1L, firstUserDto);
    }

    @Test
    void getAllUserTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(firstUserDto, secondUserDto));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(firstUserDto, secondUserDto))));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(userService.getUserById(1L)).thenReturn(firstUserDto);

        mvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUserDto.getEmail()), String.class));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }
}