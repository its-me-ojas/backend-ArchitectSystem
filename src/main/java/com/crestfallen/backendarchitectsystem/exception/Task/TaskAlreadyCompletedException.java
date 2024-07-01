package com.crestfallen.backendarchitectsystem.exception.Task;

public class TaskAlreadyCompletedException extends Throwable {
    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
