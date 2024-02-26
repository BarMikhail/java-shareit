package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfter(long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndDateBefore(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStartDateAfter(long bookerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status, Sort sort);


    List<Booking> findAllByItemOwnerId(Long userId, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(long ownerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByItemOwnerIdAndEndDateBefore(long ownerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartDateAfter(long ownerId, LocalDateTime dateTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStatus(long ownerId, BookingStatus status, Sort sort);

    Optional<Booking> findFirstByItem_IdAndStartDateLessThanEqualOrderByEndDateDesc(Long itemId, LocalDateTime beforeDate);

    Optional<Booking> findFirstByItem_IdAndStartDateGreaterThanOrderByEndDateAsc(Long itemId, LocalDateTime beforeDate);

    boolean existsByItemIdAndBookerIdAndStatusAndEndDateBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime endDate);
}
