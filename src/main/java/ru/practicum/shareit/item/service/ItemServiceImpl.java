package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(Long userId, ItemRequestDto itemRequestDto) {
        User user = checkUser(userId);
        Item item = ItemMapper.toItemRequest(itemRequestDto, user);
        return ItemMapper.toItemDTO(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId) {
        User user = checkUser(userId);
        Item item = checkItem(itemId);

        if (StringUtils.hasText(itemDto.getName())) {
            item.setName(itemDto.getName());
        }
        if (StringUtils.hasText(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        if (item.getOwner().equals(user)) {
            item.setOwner(user);
        }
        itemRepository.save(item);
        return ItemMapper.toItemDTO(item);
    }

    @Override
    public ItemDtoBooking getItemById(Long itemId, Long userId) {
        Item item = checkItem(itemId);
        User owner = item.getOwner();
        if (userId.equals(owner.getId())) {
            return getItemDtoBooking(getCommentListByItem(itemId), item);
        } else {
            return ItemMapper.toItemDtoBooking(item, getCommentListByItem(itemId));
        }
    }

    @Override
    public List<ItemDtoBooking> getAllItemByOwnerId(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(Sort.Direction.DESC, "created"))
                .stream()
                .collect(Collectors.groupingBy(Comment::getItem));

        Map<Item, List<Booking>> bookings = bookingRepository.findBookingsForItems(items)
                .stream()
                .collect(Collectors.groupingBy(Booking::getItem));

        List<ItemDtoBooking> itemDtoBookings = new ArrayList<>();

        for (Item item : items) {
            List<Comment> itemComments = comments.getOrDefault(item, Collections.emptyList());
            List<CommentDto> commentDtoList = itemComments.stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());

            List<Booking> itemBookings = bookings.getOrDefault(item, Collections.emptyList());

            ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item, commentDtoList);

            Optional<Booking> lastBooking = itemBookings.stream()
                    .filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(Booking::getEndDate));

            Optional<Booking> nextBooking = itemBookings.stream()
                    .filter(booking -> booking.getStartDate().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(Booking::getEndDate));

            if (lastBooking.isPresent()) {
                itemDtoBooking.setLastBooking(BookingMapper.toBookingOwnerDto(lastBooking.get()));

                if (nextBooking.isPresent()) {
                    itemDtoBooking.setNextBooking(BookingMapper.toBookingOwnerDto(nextBooking.get()));
                } else {
                    itemDtoBooking.setNextBooking(null);
                }
            } else {
                itemDtoBooking.setLastBooking(null);
                itemDtoBooking.setNextBooking(null);
            }


            itemDtoBookings.add(itemDtoBooking);
        }

        return itemDtoBookings;
    }

    @Override
    public List<ItemDto> searchItems(String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.searchItemByText(searchText);
        return items.stream()
                .map(ItemMapper::toItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentRequestDto commentRequestDto, Long userId, Long itemId) {
        Comment comment = CommentMapper.toCommentRequest(commentRequestDto);

        User user = checkUser(userId);
        Item item = checkItem(itemId);
        if (!bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndDateBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new InvalidDataException("Не удалось найти товар для этого пользователя");
        }
        comment.setItem(item);
        comment.setUser(user);
        log.info("{}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }


    private Item checkItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Нет такой вещи"));
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Нет такой пользователя"));
    }

    private List<CommentDto> getCommentListByItem(Long itemId) {
        List<Comment> comments = commentRepository.findCommentsByItemId(itemId);
        List<CommentDto> commentDtoList = comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        return commentDtoList.isEmpty() ? new ArrayList<>() : commentDtoList;
    }

    private ItemDtoBooking getItemDtoBooking(List<CommentDto> commentListByItem, Item item) {
        ItemDtoBooking itemDtoBooking = ItemMapper.toItemDtoBooking(item, commentListByItem);

        Optional<Booking> lastBooking = bookingRepository
                .findFirstByItem_IdAndStartDateLessThanEqualOrderByEndDateDesc(item.getId(), LocalDateTime.now());
        Optional<Booking> nextBooking = bookingRepository
                .findFirstByItem_IdAndStartDateGreaterThanOrderByEndDateAsc(item.getId(), LocalDateTime.now());

        if (lastBooking.isPresent()) {
            itemDtoBooking.setLastBooking(BookingMapper.toBookingOwnerDto(lastBooking.get()));

            if (nextBooking.isPresent()) {
                itemDtoBooking.setNextBooking(BookingMapper.toBookingOwnerDto(nextBooking.get()));
            } else {
                itemDtoBooking.setNextBooking(null);
            }
        } else {
            itemDtoBooking.setLastBooking(null);
            itemDtoBooking.setNextBooking(null);
        }

        return itemDtoBooking;
    }
}
