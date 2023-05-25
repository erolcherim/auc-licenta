package com.unibuc.auclicenta.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds to complete transaction");
    }
}
