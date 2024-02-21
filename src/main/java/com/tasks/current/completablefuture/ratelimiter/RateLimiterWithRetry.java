package com.tasks.current.completablefuture.ratelimiter;

import java.util.concurrent.CompletableFuture;

public class RateLimiterWithRetry {
    private final int maxRetries;
    private final long retryDilayMillis;

    public RateLimiterWithRetry(int maxTokens, int refillRatePerSecond,
                                int maxRetries, long retryDilayMillis) {
        this.maxRetries = maxRetries;
        this.retryDilayMillis = retryDilayMillis;
        RateScheduler.scheduleTokenRefill(maxTokens, refillRatePerSecond);
    }

    public CompletableFuture<String> executeWithRateLimit(RateLimiterTask rateLimiterTask) {
        return retryTask(maxRetries + 1, rateLimiterTask);
    }

    private CompletableFuture<String> retryTask(int maxRetries, RateLimiterTask task) {
        CompletableFuture<String> future = new CompletableFuture<>();
        retryTaskRecursive(maxRetries, task, future);
        return future;
    }

    private void retryTaskRecursive(int remainingRetries, RateLimiterTask task, CompletableFuture<String> future) {
        CompletableFuture.runAsync(() -> {

            if (RateScheduler.availableTokens.getAndDecrement() > 0) {
                String result = task.execute();
                future.complete(result);
            } else {
                if (remainingRetries > 0) {
                    System.out.println(Thread.currentThread().getName() + ": Task[" + task.id() + "] out of limit. Retrying...");
                    try {
                        Thread.sleep(retryDilayMillis);
                    } catch (InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                    retryTaskRecursive(remainingRetries - 1, task, future);
                } else {
                    future.completeExceptionally(new RuntimeException("Task[" + task.id() + "] failed: no retries left!"));
                }
            }
        });
    }
}