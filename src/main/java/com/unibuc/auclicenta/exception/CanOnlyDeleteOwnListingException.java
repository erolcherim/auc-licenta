package com.unibuc.auclicenta.exception;

public class CanOnlyDeleteOwnListingException extends RuntimeException {
    public CanOnlyDeleteOwnListingException(){
        super("Can only delete/edit own listings");
    }
}
