package ru.clevertec.cashreceipt.exception;

public class PdfServiceException extends Exception {

    public PdfServiceException() {
    }

    public PdfServiceException(String message) {
        super(message);
    }

    public PdfServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
