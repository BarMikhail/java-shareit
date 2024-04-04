package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemPostDto;


import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @Validated(Create.class) @RequestBody ItemPostDto itemPostDto) {
        log.info("Посмотрим какой запрос {}, какой пользователь {}", itemPostDto, userId);
        return itemRequestClient.addItemRequest(userId, itemPostDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable("requestId") long requestId,
                                                @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Поиск определенного запроса, id  запроса = {}", requestId);
        return itemRequestClient.getItemRequestById(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestByUser(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Вывод всех запросов пользователя {}", userId);
        return itemRequestClient.getAllItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Вывод всего");
        return itemRequestClient.getItemRequests(from, size, userId);
    }
}
