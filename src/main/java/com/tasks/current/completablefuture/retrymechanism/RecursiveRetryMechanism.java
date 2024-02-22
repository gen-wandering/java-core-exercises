package com.tasks.current.completablefuture.retrymechanism;

import java.util.concurrent.CompletableFuture;

public class RecursiveRetryMechanism {
    public static void main(String[] args) throws InterruptedException {
        var res = retryTask(5, 1000, () -> {
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

    public static <T> CompletableFuture<T> retryTask(int maxRetries, long retryDilayMillis, Task<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        retryTaskRecursive(maxRetries, retryDilayMillis, task, future);
        return future;
    }

    private static <T> void retryTaskRecursive(int remainingRetries, long retryDilayMillis,
                                               Task<T> task, CompletableFuture<T> future) {
        CompletableFuture.runAsync(() -> {
            try {
                T result = task.execute();
                future.complete(result);
            } catch (Exception e) {
                if (remainingRetries > 0) {
                    System.out.println(Thread.currentThread().getName() + ": " + e.getMessage() + " Retrying...");
                    try {
                        Thread.sleep(retryDilayMillis);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    retryTaskRecursive(remainingRetries - 1, retryDilayMillis, task, future);
                } else {
                    future.completeExceptionally(e);
                }
            }
        });
    }
}