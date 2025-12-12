package com.example.todo.exception;

public class IdNullException extends BaseException {
    public IdNullException(String error, int code) {
        super(error);
        this.code = code;
    }
}
