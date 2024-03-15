package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;

    private User user;
    private ItemRequest itemRequest;
    private ItemPostDto itemPostDto;
    private ItemRequestDtoOut itemRequestDtoOut;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test test")
                .user(user)
                .created(LocalDateTime.now())
                .build();

        itemPostDto = ItemPostDto.builder()
                .description("test test")
                .build();

        itemRequestDtoOut = ItemRequestDtoOut.builder()
                .id(1L)
                .description("test test")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void addItemRequest() {

        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toItemRequestDtoOut(itemRequest);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDtoOut itemReq = itemRequestService.addItemRequest(1L, itemPostDto);

        assertEquals(itemReq.getDescription(), itemPostDto.getDescription());
        assertEquals(itemRequestDtoOut, itemReq);
    }

    @Test
    void getItemRequestById() {


        ItemRequestDtoOut itemRequestDtoOut = ItemRequestMapper.toItemRequestDtoOut(itemRequest);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));

        ItemRequestDtoOut itemReq = itemRequestService.getItemRequestById(1L, 1);

        assertEquals(itemRequestDtoOut, itemReq);
    }

    @Test
    void getAllItemRequestsByUserId() {

        List<ItemRequestDtoOut> itemRequestDtoOut = List.of(ItemRequestMapper.toItemRequestDtoOut(itemRequest));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.findAllByUserId(anyLong())).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> itemReq = itemRequestService.getAllItemRequestsByUserId(1L);


        assertNotNull(itemReq);
        assertEquals(itemRequestDtoOut, itemReq);


    }

    @Test
    void getItemRequests() {

        List<ItemRequestDtoOut> itemRequestDtoOut = List.of(ItemRequestMapper.toItemRequestDtoOut(itemRequest));

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDtoOut> itemReq = itemRequestService.getItemRequests(0, 10, 1);

        assertNotNull(itemReq);
        assertEquals(itemRequestDtoOut, itemReq);
    }
}