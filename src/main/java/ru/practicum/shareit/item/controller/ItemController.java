package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.additionally.Create;
import ru.practicum.shareit.additionally.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                           @Validated(Create.class) @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Посмотрим что передается {}, и какому юзеру {}", itemRequestDto, userId);
        return itemService.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable("itemId") long itemId,
                              @RequestBody @Validated(Update.class) ItemDto itemDto,
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

    @GetMapping
    public List<ItemDtoBooking> getAllItemByUser(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Просмотр владельцем всех вещей, id = {} пользователя", userId);
        return itemService.getAllItemByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск вещи арендатором, text = {}", text);
        return itemService.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(X_SHARER_USER_ID) long userId,
                                    @Validated(Create.class) @RequestBody CommentRequestDto commentRequestDto,
                                    @PathVariable("itemId") long itemId) {
        log.info("Добавление нового отзыва {} об вещи {}", commentRequestDto, itemId);
        return itemService.createComment(commentRequestDto, userId, itemId);
    }
}
