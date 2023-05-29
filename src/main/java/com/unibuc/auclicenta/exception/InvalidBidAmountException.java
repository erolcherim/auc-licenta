package com.unibuc.auclicenta.exception;

public class InvalidBidAmountException extends RuntimeException{
    public InvalidBidAmountException(){
        super("Bid must be at least 10% higher than the starting price");
    }
}
