package com.unibuc.auclicenta.exception;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(){
        super("Entity already exists");
    }
}
