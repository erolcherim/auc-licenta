package com.unibuc.auclicenta.exception;

public class SamePasswordException extends RuntimeException{
    public SamePasswordException(){
        super("New password can't be old password");
    }
}
