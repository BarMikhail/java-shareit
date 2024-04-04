package ru.practicum.shareit.exception;

public class ExistEmailException extends RuntimeException {

    public ExistEmailException(String message) {
        super(message);
    }

    public ExistEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistEmailException() {
        super();
    }
}
