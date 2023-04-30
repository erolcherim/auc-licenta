package com.unibuc.auclicenta.exception;

public class AuthFailedException extends RuntimeException{
    public AuthFailedException(){
        super("Username or password are incorrect");
    }
}
