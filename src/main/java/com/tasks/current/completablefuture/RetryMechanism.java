package com.tasks.current.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

/*
 * Retry Mechanism:
 *    Implement a retry mechanism using CompletableFuture
 *    to execute a task multiple times until successful.
 * */

public class RetryMechanism {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var res = retryTask(5, 1, () -> {
            if (Math.random() < 0.8) {
                throw new RuntimeException("Task failed!");
            }
            return "Success";
        }).thenAccept(result -> System.out.println("Task completed successfully with result: " + result));

        res.exceptionally(throwable -> {
            System.out.println(throwable.getMessage());
            return null;
        }).join();
    }

    public static <T> CompletableFuture<T> retryTask(int maxRetries, long delayMillis, Task<T> task) {
        Supplier<T> supplier = () -> {
            try {
                Thread.sleep(delayMillis);
                return task.execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        var future = CompletableFuture.supplyAsync(supplier);

        for (int i = 0; i < maxRetries; i++) {
            future = future.exceptionallyAsync(throwable -> supplier.get());
        }
        return future;
    }

    @FunctionalInterface
    interface Task<T> {
        T execute() throws Exception;
    }
}