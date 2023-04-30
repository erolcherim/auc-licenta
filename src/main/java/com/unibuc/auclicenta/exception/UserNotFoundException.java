package com.unibuc.auclicenta.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("A user with this identifier does not exist");
    }
}
