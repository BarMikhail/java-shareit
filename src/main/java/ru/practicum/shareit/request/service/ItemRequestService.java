package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoOut addItemRequest(long userId, ItemPostDto itemPostDto);

    ItemRequestDtoOut getItemRequestById(long requestId, long userId);

    List<ItemRequestDtoOut> getAllItemRequestsByUserId(long userId);

    List<ItemRequestDtoOut> getItemRequests(Integer from, Integer size, long userId);
}
