package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Нет такого пользователя");
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDTO(itemRepository.addItem(item));
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long userId) {
        Item item = itemRepository.getItemById(itemId);

        if (item == null) {
            throw new NotFoundException("Нет такой вещи");
        }

        if (userId != item.getOwner().getId()) {
            throw new NotFoundException("Не тот владелец");
        }

        if (StringUtils.hasLength(itemDto.getName())) {
            item.setName(itemDto.getName());
        }
        if (StringUtils.hasLength(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        itemRepository.updateItem(item, itemId);
        return ItemMapper.toItemDTO(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException("Нет такой вещи");
        }
        return ItemMapper.toItemDTO(item);
    }

    @Override
    public List<ItemDto> getAllItem(long userId) {
        List<Item> items = itemRepository.findByUserId(userId);
        List<ItemDto> itemDtos = new ArrayList<>();

        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDTO(item);
            itemDtos.add(itemDto);
        }

        return itemDtos;
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (!StringUtils.hasLength(searchText)) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.findBySearch(searchText);
        List<ItemDto> itemDtos = new ArrayList<>();

        for (Item item : items) {
            ItemDto itemDto = ItemMapper.toItemDTO(item);
            itemDtos.add(itemDto);
        }

        return itemDtos;
    }
}
