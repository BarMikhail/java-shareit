package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                          @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Посмотрим что передается {}, и какому юзеру {}", itemRequestDto, userId);
        return itemClient.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable("itemId") long itemId,
                                             @RequestBody @Validated(Update.class) ItemRequestDto itemRequestDto,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Посмотрим что обновляется {}, id {} вещи, и у какого юзера {}", itemRequestDto, itemId, userId);
        return itemClient.updateItem(itemId, itemRequestDto, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable("itemId") long itemId,
                                              @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Поиск определенной вещи, id = {}", itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemByUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Просмотр владельцем всех вещей, id = {} пользователя", userId);
        return itemClient.getAllItemByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск вещи арендатором, text = {}", text);

        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                @Validated(Create.class) @RequestBody CommentRequestDto commentRequestDto,
                                                @PathVariable("itemId") long itemId) {
        log.info("Добавление нового отзыва {} об вещи {}", commentRequestDto, itemId);
        return itemClient.createComment(commentRequestDto, userId, itemId);
    }
}
