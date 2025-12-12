package com.example.todo.exception;

public class TodoNotFoundException extends BaseException{
    public TodoNotFoundException(String error) {
        super(error);
        this.code = 404;
    }
}
