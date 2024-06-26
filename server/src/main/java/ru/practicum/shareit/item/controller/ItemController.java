package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.additionally.Constant.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Посмотрим что передается {}, и какому юзеру {}", itemRequestDto, userId);
        return itemService.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId") long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Посмотрим что обновляется {}, id {} вещи, и у какого юзера {}", itemDto, itemId, userId);
        return itemService.updateItem(itemId, itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoBooking getItemById(@PathVariable("itemId") long itemId,
                                      @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Поиск определенной вещи, id = {}", itemId);
        return itemService.getItemById(itemId, userId);
    }


    //вылравыалваило
    @GetMapping
    public List<ItemDtoBooking> getAllItemByUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Просмотр владельцем всех вещей, id = {} пользователя", userId);
        return itemService.getAllItemByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск вещи арендатором, text = {}", text);
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                    @RequestBody CommentRequestDto commentRequestDto,
                                    @PathVariable("itemId") long itemId) {
        log.info("Добавление нового отзыва {} об вещи {}", commentRequestDto, itemId);
        return itemService.createComment(commentRequestDto, userId, itemId);
    }
}
