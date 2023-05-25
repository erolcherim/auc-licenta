package com.unibuc.auclicenta.exception;

public class InvalidTopUpAmountException extends RuntimeException {
    public InvalidTopUpAmountException(){
        super("Top-up must be at least 5 units");
    }
}
