package com.inventory.exception;

public class NoProductFoundException extends Exception {
    public NoProductFoundException(String message){
        super(message);
    }
}
