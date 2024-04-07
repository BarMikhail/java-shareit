package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({InvalidDataException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseError handleInvalidDataException(final RuntimeException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new CustomResponseError(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomResponseError handleIllegalArgumentException(final RuntimeException e) {
        log.debug("Получен статус 400 Bad Request {}", e.getMessage(), e);
        return new CustomResponseError("Unknown state: UNSUPPORTED_STATUS");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public CustomResponseError handleException(final Throwable e) {
        log.debug("Получен статус 500 Internal Server Error {}", e.getMessage(), e);
        return new CustomResponseError(e.getMessage());
    }
}
