package com.example.todo.exception;

public class IdNullException extends RuntimeException {

    private int code = 500;
    public IdNullException(String error, int code) {
        super(error);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
