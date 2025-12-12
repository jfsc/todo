package com.example.todo.exception;

public class BaseException extends RuntimeException {
    protected int code = 500;

    public BaseException(String error) {
        super(error);
    }

    public int getCode() { return code; }
}
