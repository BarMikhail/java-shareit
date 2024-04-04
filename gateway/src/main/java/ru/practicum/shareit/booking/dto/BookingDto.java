package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class BookingDto {
//    @NotNull
    private Long itemId;

//    @Future
//    @NotNull
    private LocalDateTime start;

//    @Future
//    @NotNull
    private LocalDateTime end;
}
