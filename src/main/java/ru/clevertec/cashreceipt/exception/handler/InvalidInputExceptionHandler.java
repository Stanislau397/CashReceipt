package ru.clevertec.cashreceipt.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.cashreceipt.exception.CashierException;
import ru.clevertec.cashreceipt.exception.InvalidInputException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class InvalidInputExceptionHandler {

    @ExceptionHandler(value = {InvalidInputException.class})
    public ResponseEntity<CashierException> handleEntityNotFoundException(InvalidInputException exception) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;
        CashierException apiException = new CashierException(
                exception.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notFound);
    }
}
