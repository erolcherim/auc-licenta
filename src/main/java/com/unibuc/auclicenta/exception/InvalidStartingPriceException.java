package com.unibuc.auclicenta.exception;

public class InvalidStartingPriceException extends RuntimeException {
    public InvalidStartingPriceException(){
        super("Starting price must be higher than 0 and should be an integer value");
    }
}
