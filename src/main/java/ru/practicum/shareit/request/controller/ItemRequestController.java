package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additionally.Create;

import ru.practicum.shareit.request.dto.ItemPostDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoOut addItemRequest(@RequestHeader(X_SHARER_USER_ID) long userId,
                                            @Validated(Create.class) @RequestBody ItemPostDto itemPostDto) {
        log.info("Посмотрим какой запрос {}, какой пользователь {}", itemPostDto, userId);
        return itemRequestService.addItemRequest(userId, itemPostDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut getItemRequestById(@PathVariable("requestId") long requestId,
                                                @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Поиск определенного запроса, id  запроса = {}", requestId);
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestDtoOut> getAllItemRequestByUser(@RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Вывод всех запросов пользователя {}", userId);
        return itemRequestService.getAllItemRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> getItemRequests(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                                   @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Вывод всего");
        return itemRequestService.getItemRequests(from, size, userId);
    }
}
