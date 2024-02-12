package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private int id;
    private Item item;
    private User user;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean confirmed;

}
