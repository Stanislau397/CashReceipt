package ru.clevertec.cashreceipt.exception;

public class EntityAlreadyExistsException extends RuntimeException{

    public EntityAlreadyExistsException() {
    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
