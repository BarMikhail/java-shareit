package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verifyNoInteractions;
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

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private User user;
    private Item firstItem;
    private Item secondItem;
    private Comment firstComment;
    private Comment secondComment;
    private CommentRequestDto commentRequestDto;
    private Booking firstBooking;
    private Booking secondBooking;
    private Booking thirdBooking;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void beforeEach() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@ya.ru")
                .build();

        firstItem = Item.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .owner(user)
                .available(true)
                .build();

        secondItem = Item.builder()
                .id(2L)
                .name("test")
                .description("test test")
                .owner(user)
                .available(true)
                .build();

        firstComment = Comment.builder()
                .id(1L)
                .text("test")
                .user(user)
                .item(firstItem)
                .build();

        secondComment = Comment.builder()
                .id(2L)
                .text("test")
                .user(user)
                .item(secondItem)
                .build();

        firstBooking = Booking.builder()
                .id(1L)
                .item(firstItem)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        secondBooking = Booking.builder()
                .id(2L)
                .item(firstItem)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .build();

        thirdBooking = Booking.builder()
                .id(3L)
                .item(firstItem)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .startDate(LocalDateTime.now().plusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .build();

        commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .name("test")
                .description("test test")
                .available(true)
                .requestId(1L)
                .build();
    }

    @Test
    void addItemNullRequestItemTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.save(any(Item.class))).thenReturn(firstItem);

        ItemDto itemDto = itemService.addItem(anyLong(), itemRequestDto);

        assertNotNull(itemDto);
        assertEquals(firstItem.getId(), itemDto.getId());
        assertEquals(firstItem.getName(), itemDto.getName());
        assertNull(itemDto.getRequestId());
    }

    @Test
    void addItemTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .build();
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(itemRequest);
        firstItem.setItemRequest(itemRequest);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(firstItem);

        ItemDto itemDto = itemService.addItem(anyLong(), itemRequestDto);

        assertNotNull(itemDto);
        assertEquals(firstItem.getId(), itemDto.getId());
        assertNotNull(itemDto.getRequestId());
    }

    @Test
    void updateItemTest() {
        Item updateItem = Item.builder()
                .id(1L)
                .name("test update")
                .description("tes update test")
                .owner(user)
                .available(false)
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(updateItem));

        ItemDto update = itemService.updateItem(1L, ItemMapper.toItemDTO(updateItem), 1L);

        assertEquals("test update", update.getName());
        assertEquals("tes update test", update.getDescription());
    }

    @Test
    void getItemByIdTest() {
        ItemDtoBooking itemDto = ItemDtoBooking.builder()
                .id(1L)
                .name("test")
                .description("test test")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstItem));

        ItemDtoBooking itemDtoBooking = itemService.getItemById(1L, 1L);

        assertEquals(itemDto, itemDtoBooking);
        assertNull(itemDtoBooking.getNextBooking());
        assertNull(itemDtoBooking.getLastBooking());
    }

    @Test
    void shouldReturnLastBookingWhenItemExistsTest() {
        Booking lastBooking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(firstItem)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().minusDays(1))
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartDateBeforeOrderByStartDateDesc(anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(Optional.of(lastBooking));

        ItemDtoBooking result = itemService.getItemById(1L, 1L);

        assertNotNull(result.getLastBooking());
    }

    @Test
    void shouldReturnNextBookingWhenItemExistsTest() {
        Booking nextBooking = Booking.builder()
                .id(1L)
                .booker(user)
                .item(firstItem)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().minusDays(1))
                .build();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingRepository.findFirstByItemIdAndStatusAndStartDateAfterOrderByStartDateAsc(anyLong(),
                any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(Optional.of(nextBooking));

        ItemDtoBooking result = itemService.getItemById(1L, 1L);

        assertNotNull(result.getNextBooking());
    }

    @Test
    void testGetItemByIdForNonOwnerTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(firstItem));
        ItemDtoBooking result = itemService.getItemById(1L, 2L);

        assertNotNull(result);
        assertEquals(firstItem.getId(), result.getId());
        verifyNoInteractions(userRepository);
    }

    @Test
    void getAllItemByOwnerIdTest() {
        List<Item> items = Arrays.asList(firstItem, secondItem);

        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(items);

        Map<Item, List<Comment>> comments = new HashMap<>();
        comments.put(firstItem, Arrays.asList(firstComment, secondComment));
        comments.put(secondItem, Collections.singletonList(firstComment));
        when(commentRepository.findByItemIn(items, Sort.by(Sort.Direction.DESC, "created")))
                .thenReturn(comments.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue().stream())
                        .collect(Collectors.toList()));

        Map<Item, List<Booking>> bookings = new HashMap<>();
        bookings.put(firstItem, Arrays.asList(firstBooking, secondBooking, thirdBooking));
        when(bookingRepository.findBookingsForItems(items))
                .thenReturn(bookings.entrySet()
                        .stream()
                        .flatMap(entry -> entry.getValue().stream())
                        .collect(Collectors.toList()));

        List<ItemDtoBooking> result = itemService.getAllItemByOwnerId(1L, 0, 10);

        assertNotNull(result);
        assertEquals(items.size(), result.size());

        ItemDtoBooking itemDtoBooking1 = result.get(0);
        assertEquals(firstItem.getName(), itemDtoBooking1.getName());
        assertEquals(2, itemDtoBooking1.getComments().size());
        assertNotNull(itemDtoBooking1.getLastBooking());
        assertNotNull(itemDtoBooking1.getNextBooking());

        ItemDtoBooking itemDtoBooking2 = result.get(1);
        assertEquals(secondItem.getName(), itemDtoBooking2.getName());
        assertEquals(1, itemDtoBooking2.getComments().size());
        assertNull(itemDtoBooking2.getLastBooking());
        assertNull(itemDtoBooking2.getNextBooking());
    }

    @Test
    void searchItemsTest() {
        List<Item> items = List.of(firstItem);
        String text = "te";
        when(itemRepository.searchItemByText(anyString(), any(Pageable.class))).thenReturn(items);

        List<ItemDto> itemDtos = itemService.searchItems(text, 0, 10);

        assertEquals(itemDtos.get(0).getId(), items.get(0).getId());
    }

    @Test
    void searchItemsWhenSearchTextIsEmptyTest() {
        List<ItemDto> result = itemService.searchItems("", 0, 10);

        assertEquals(Collections.emptyList(), result);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void searchItemsWhenSearchTextIsNullTest() {
        List<ItemDto> result = itemService.searchItems(null, 0, 10);

        assertEquals(Collections.emptyList(), result);
        verifyNoInteractions(itemRepository);
    }

    @Test
    void createCommentTest() {
        CommentDto commentDto = CommentMapper.toCommentDto(firstComment);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndDateBefore(anyLong(), anyLong(), any(BookingStatus.class), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(firstComment);

        CommentDto com = itemService.createComment(commentRequestDto, 1L, 1L);

        assertEquals(com, commentDto);

    }

    @Test
    void createCommentThrowsInvalidDataExceptionTest() {
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(firstItem));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndDateBefore(anyLong(),
                anyLong(), any(BookingStatus.class), any(LocalDateTime.class))).thenReturn(false);


        InvalidDataException requestNotFoundException = assertThrows(InvalidDataException.class,
                () -> itemService.createComment(commentRequestDto, 1L, 1L));

        assertEquals(requestNotFoundException.getMessage(), "Не удалось найти товар для этого пользователя");
    }
}