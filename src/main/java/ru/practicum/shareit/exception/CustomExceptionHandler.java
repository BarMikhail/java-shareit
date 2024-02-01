package ru.practicum.shareit.exception;

public class CustomExceptionHandler extends RuntimeException {

    public CustomExceptionHandler(String message) {
        super(message);
    }

    public CustomExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomExceptionHandler() {
        super();
    }
}
