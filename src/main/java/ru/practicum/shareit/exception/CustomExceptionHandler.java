package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public CustomResponseError handleEmailException(final ExistEmailException e) {
        return new CustomResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public CustomResponseError handleOwnerItemException(final NotFoundException e) {
        return new CustomResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public CustomResponseError handleNotFoundArgumentException(final MethodArgumentNotValidException e) {
        return new CustomResponseError(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public CustomResponseError handleException(final Throwable e) {
        return new CustomResponseError(e.getMessage());
    }
}
