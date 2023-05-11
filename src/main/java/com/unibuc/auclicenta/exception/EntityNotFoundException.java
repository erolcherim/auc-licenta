package com.unibuc.auclicenta.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(){
        super("Entity not found");
    }
}
