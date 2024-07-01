package com.crestfallen.backendarchitectsystem.exception.Task;

public class TaskStillPendingException extends RuntimeException {
    public TaskStillPendingException(String message) {
        super(message);
    }
}
