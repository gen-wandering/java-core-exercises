package com.tasks.dynamicqueue.exceptions;

public class QueueFullException extends RuntimeException {
    public QueueFullException(String message) {
        super(message);
    }
}
