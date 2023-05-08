package com.unibuc.auclicenta.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){
        super("A user already exists for this email address");
    }
}
