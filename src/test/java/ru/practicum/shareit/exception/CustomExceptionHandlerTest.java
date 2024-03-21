package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomExceptionHandlerTest {

    private final CustomExceptionHandler error = new CustomExceptionHandler();

    @Test
    public void handleEmailExceptionTest() {
        ExistEmailException e = new ExistEmailException("test");
        CustomResponseError er = error.handleEmailException(e);

        assertNotNull(er);
        assertEquals(er.getError(), e.getMessage());
    }

    @Test
    public void handleOwnerItemExceptionTest() {
        NotFoundException e = new NotFoundException("test");
        CustomResponseError er = error.handleOwnerItemException(e);

        assertNotNull(er);
        assertEquals(er.getError(), e.getMessage());
    }

    @Test
    public void handleInvalidDataExceptionTest() {
        InvalidDataException e = new InvalidDataException("test");
        CustomResponseError er = error.handleInvalidDataException(e);

        assertNotNull(er);
        assertEquals(er.getError(), e.getMessage());
    }

    @Test
    public void handleExceptionTest() {
        Throwable e = new Throwable("test");
        CustomResponseError er = error.handleException(e);

        assertNotNull(er);
        assertEquals(er.getError(), e.getMessage());
    }

    @Test
    public void handleIllegalArgumentExceptionTest() {
        RuntimeException e = new RuntimeException();
        CustomResponseError er = error.handleIllegalArgumentException(e);

        assertNotNull(er);
        assertEquals(er.getError(), "Unknown state: UNSUPPORTED_STATUS");
    }

}