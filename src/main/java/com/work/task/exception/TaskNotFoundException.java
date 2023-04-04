package com.work.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskNotFoundException(String message) {
        this(message, null);
    }
}
