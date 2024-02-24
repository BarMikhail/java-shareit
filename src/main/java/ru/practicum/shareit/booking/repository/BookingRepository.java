package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDateDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateAsc(long bookerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(long bookerId, LocalDateTime dateTime);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDateDesc(long bookerId, BookingStatus status);


    List<Booking> findAllByItemOwnerIdOrderByStartDateDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateAsc(long ownerId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStartDateAfterOrderByStartDateDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDateDesc(long ownerId, BookingStatus status);


    @Query(value = "SELECT * FROM bookings b " +
            "WHERE b.item_id = ?1 AND b.start_date <= CAST (?2 AS timestamp) " +
            "ORDER BY end_date DESC limit 1", nativeQuery = true)
    Optional<Booking> findFirstByItem_IdAndStartDateBeforeOrderByEndDateDesc(Long itemId, LocalDateTime beforeDate);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE b.item_id = ?1 AND b.start_date >= CAST (?2 AS timestamp) " +
            "ORDER BY end_date ASC limit 1", nativeQuery = true)
    Optional<Booking> findFirstByItem_IdAndStartDateAfterOrderByEndDateAsc(Long itemId, LocalDateTime afterDate);

    Optional<Booking> findFirstByItemIdAndBookerIdAndStatusAndEndDateBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime endDate);


}