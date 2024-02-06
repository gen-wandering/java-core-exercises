package com.tasks.completed.dynamicqueue.exceptions;

public class QueueEmptyException extends RuntimeException {
    public QueueEmptyException(String message) {
        super(message);
    }
}
