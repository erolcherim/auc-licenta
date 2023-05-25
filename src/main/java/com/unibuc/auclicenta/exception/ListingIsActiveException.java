package com.unibuc.auclicenta.exception;

public class ListingIsActiveException extends RuntimeException {
    public ListingIsActiveException(){
        super("Listing is active and cannot be deleted");
    }
}
