package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDtoOut addItemRequest(long userId, ItemPostDto itemPostDto) {
        User user = checkUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.toRequest(itemPostDto);
        itemRequest.setUser(user);
        return ItemRequestMapper.toItemRequestDtoOut(itemRequestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDtoOut getItemRequestById(long requestId, long userId) {
        checkUser(userId);
        ItemRequest itemRequest = checkItemRequest(requestId);
        return ItemRequestMapper.toItemRequestDtoOut(itemRequest);
    }

    @Override
    public List<ItemRequestDtoOut> getAllItemRequestsByUserId(long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId);
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoOut> getItemRequests(Integer from, Integer size, long userId) {
        checkUser(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdNotOrderByCreatedDesc(userId,
                PageRequest.of(from / size, size));
        return itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDtoOut)
                .collect(Collectors.toList());
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такой пользователя"));
    }

    private ItemRequest checkItemRequest(Long requestId) {
        return itemRequestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Что-то пошло не так"));
    }
}

