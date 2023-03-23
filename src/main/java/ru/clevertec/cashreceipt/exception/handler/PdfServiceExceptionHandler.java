package ru.clevertec.cashreceipt.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.cashreceipt.exception.CashierException;
import ru.clevertec.cashreceipt.exception.PdfServiceException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class PdfServiceExceptionHandler {

    @ExceptionHandler(value = {PdfServiceException.class})
    public ResponseEntity<CashierException> handlePdfServiceExceptionException(PdfServiceException exception) {
        HttpStatus notFound = HttpStatus.INTERNAL_SERVER_ERROR;
        CashierException apiException = new CashierException(
                exception.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, notFound);
    }
}
