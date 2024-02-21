package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.InvalidDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDtoResponse createBookingRequest(BookingDto bookingDto, Long userId) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new InvalidDataException("Дата пошла погулять");
        }

        User booker = checkUser(userId);
        Item item = checkItem(bookingDto.getItemId());
        User owner = item.getOwner();
        if (!item.getAvailable()) {
            throw new InvalidDataException("Товар занят");
        }
        if (owner.getId().equals(userId)) {
            throw new NotFoundException("Зачем владельцу бронировать свой товар?");
        } else {
            Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
            return BookingMapper.toBookingResponse(bookingRepository.save(booking), ItemMapper.toItemDTO(item));
        }
    }

    @Override
    @Transactional
    public BookingDtoResponse updateBookingStatusByOwner(Long bookingID, Long userId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingID)
                .orElseThrow(() -> new NotFoundException("Нет такой брони"));
        ItemDto itemDto = ItemMapper.toItemDTO(checkItem(booking.getItem().getId()));
        User owner = booking.getItem().getOwner();

        if (booking.getStatus().equals(BookingStatus.APPROVED) && approved) {
            throw new InvalidDataException("Бронирование вещи одобрено ранее.");
        }
        if (booking.getStatus().equals(BookingStatus.REJECTED) && !approved) {
            throw new InvalidDataException("Бронирование вещи отклонено ранее.");
        }

        if (userId.equals(owner.getId())) {
            if (approved) {
                booking.setStatus(BookingStatus.APPROVED);
            } else {
                booking.setStatus(BookingStatus.REJECTED);
            }

            return BookingMapper.toBookingResponse(bookingRepository.save(booking), itemDto);
        } else {
            throw new NotFoundException("Только владелец может подтвердить бронирование предмета.");
        }
    }

    @Override
    public BookingDtoResponse getBookingDetails(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено."));
        ItemDto itemDto = ItemMapper.toItemDTO(checkItem(booking.getItem().getId()));

        if (userId.equals(booking.getBooker().getId()) || userId.equals((booking.getItem().getOwner().getId()))) {
            return BookingMapper.toBookingResponse(booking, itemDto);
        } else {
            throw new NotFoundException("Только автор или владелец может проверить детали бронирования.");
        }
    }

    @Override
    public List<BookingDtoResponse> getAllBooking(BookingState state, Long userId) {
        return null;
    }

    @Override
    public List<BookingDtoResponse> getAllBookingByOwner(BookingState state, Long userId) {
        return null;
    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item checkItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар не найден"));
    }
}
