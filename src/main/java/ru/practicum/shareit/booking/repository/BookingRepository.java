package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

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

    boolean existsByItemIdAndBookerIdAndStatusAndEndDateBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime endDate);

    @Query("SELECT b FROM Booking b JOIN FETCH b.item WHERE b.item IN :items AND b.status = 'APPROVED'")
    List<Booking> findBookingsForItems(@Param("items") List<Item> items);


    Optional<Booking> findFirstByItemIdAndStatusAndStartDateBeforeOrderByStartDateDesc(Long itemId, BookingStatus status, LocalDateTime dateTime);

    Optional<Booking> findFirstByItemIdAndStatusAndStartDateAfterOrderByStartDateAsc(Long itemId, BookingStatus status, LocalDateTime dateTime);
}
