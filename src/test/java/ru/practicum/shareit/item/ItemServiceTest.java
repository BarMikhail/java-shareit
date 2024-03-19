package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private User user;
    private Item item;
    private Item item2;
    private Comment comment;
    private Comment comment2;
    private CommentRequestDto commentRequestDto;
    private Booking booking;
    private Booking booking2;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .owner(user)
                .available(true)
                .build();

        item2 = Item.builder()
                .id(2L)
                .name("test")
                .description("test test")
                .owner(user)
                .available(true)
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("test")
                .user(user)
                .item(item)
                .build();
        comment2 = Comment.builder()
                .id(2L)
                .text("test")
                .user(user)
                .item(item2)
                .build();

        booking = Booking.builder()
                .id(1L)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();
        booking2 = Booking.builder()
                .id(2L)
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();
        commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .name("test")
                .description("test test")
                .available(true)
                .build();
    }

    @Test
    void addItem() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto itemDto = itemService.addItem(1L, itemRequestDto);

        assertNotNull(itemDto);
        assertEquals(itemDto.getId(), item.getId());
    }

    @Test
    void updateItem() {
        Item updateItem = Item.builder()
                .id(1L)
                .name("test update")
                .description("tes update text")
                .owner(user)
                .available(false)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(updateItem));

        ItemDto update = itemService.updateItem(1L, ItemMapper.toItemDTO(updateItem) ,1L);

        assertEquals("test update",update.getName());
        assertEquals("tes update text",update.getDescription());
    }

    @Test
    void getItemById() {

//        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
//        when(commentRepository.findCommentsByItemId(1L)).thenReturn(Collections.singletonList(comment));
//        when(bookingRepository.findFirstByItemIdAndStatusAndStartDateBeforeOrderByStartDateDesc(
//                1L, BookingStatus.APPROVED, LocalDateTime.now()
//        )).thenReturn(Optional.of(booking));
//        when(bookingRepository.findFirstByItemIdAndStatusAndStartDateAfterOrderByStartDateAsc(
//                1L, BookingStatus.APPROVED, LocalDateTime.now()
//        )).thenReturn(Optional.empty());
//
//        ItemDtoBooking result = itemService.getItemById(item.getId(), user.getId());
//
//        // Assert
//        assertNotNull(result);
//        // остальные проверки

        ItemDtoBooking itemDto = ItemDtoBooking.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .available(true)
                .comments(Collections.emptyList())
                .build();


//        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        ItemDtoBooking result = itemService.getItemById(1L,1L);

        assertEquals(itemDto,result);
    }

    @Test
    void getAllItemByOwnerId() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;

        List<Item> items = Arrays.asList(item, item2);

        when(itemRepository.findAllByOwnerId(userId, PageRequest.of(from / size, size)))
                .thenReturn(items);

        Map<Item, List<Comment>> comments = new HashMap<>();
        comments.put(item, Arrays.asList(comment, comment2));
        comments.put(item2, Collections.singletonList(comment));
        when(commentRepository.findByItemIn(items, Sort.by(Sort.Direction.DESC, "created")))
                .thenReturn(comments.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue().stream())
                        .collect(Collectors.toList()));

        Map<Item, List<Booking>> bookings = new HashMap<>();
        bookings.put(item, Arrays.asList(booking, booking2));
        when(bookingRepository.findBookingsForItems(items))
                .thenReturn(bookings.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue().stream())
                        .collect(Collectors.toList()));

        List<ItemDtoBooking> result = itemService.getAllItemByOwnerId(userId, from, size);

        assertNotNull(result);
        assertEquals(items.size(), result.size());

        ItemDtoBooking itemDtoBooking1 = result.get(0);
        assertEquals(item.getName(), itemDtoBooking1.getName());
        assertEquals(2, itemDtoBooking1.getComments().size());
        assertNotNull(itemDtoBooking1.getLastBooking());
        assertNull(itemDtoBooking1.getNextBooking());

        ItemDtoBooking itemDtoBooking2 = result.get(1);
        assertEquals(item2.getName(), itemDtoBooking2.getName());
        assertEquals(1, itemDtoBooking2.getComments().size());
        assertNull(itemDtoBooking2.getLastBooking());
        assertNull(itemDtoBooking2.getNextBooking());
    }

    @Test
    void searchItems() {
        List<Item> items = List.of(item);
        String text = "te";
        when(itemRepository.searchItemByText(text, PageRequest.of(0 / 10, 10))).thenReturn(items);

        List<ItemDto> itemDtos = itemService.searchItems(text, 0, 10);

        assertEquals(itemDtos.get(0).getId(), items.get(0).getId());
    }

    @Test
    void createComment() {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndDateBefore(anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto com = itemService.createComment(commentRequestDto, 1L, 1L);

        assertEquals(com, commentDto);

    }
}