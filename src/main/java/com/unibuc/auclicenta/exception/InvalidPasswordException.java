package com.unibuc.auclicenta.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Password is invalid");
    }
}
