package com.tasks.current.completablefuture.retrymechanism;

@FunctionalInterface
public interface Task<T> {
    T execute() throws Exception;
}