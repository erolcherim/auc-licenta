package com.unibuc.auclicenta.exception;

public class FileTypeNotAcceptedException extends RuntimeException {
    public FileTypeNotAcceptedException(){
        super("Image must be jpg/jpeg");
    }
}
