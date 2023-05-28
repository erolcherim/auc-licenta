package com.unibuc.auclicenta.exception;

public class CannotBidOnOwnListingException extends RuntimeException {
    public CannotBidOnOwnListingException(){
        super("Cannot bid on own listing");
    }
}
