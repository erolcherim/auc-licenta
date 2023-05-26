package com.unibuc.auclicenta.exception;

public class ListingIsActiveException extends RuntimeException {
    public ListingIsActiveException(){
        super("Listing is not in the pre-activation stage and cannot be edited/deleted");
    }
}
