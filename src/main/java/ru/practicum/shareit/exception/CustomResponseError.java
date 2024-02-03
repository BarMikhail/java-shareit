package ru.practicum.shareit.exception;

import lombok.Data;

@Data
public class CustomResponseError {
    private final String message;
}
