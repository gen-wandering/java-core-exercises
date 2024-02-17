package com.tasks.current.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Rate Limiting:
 *    Create a rate-limiting mechanism using CompletableFuture
 *    to control the rate at which tasks are executed, ensuring
 *    they do not exceed a certain limit per unit of time.
 * */

public class RateLimiter {
    private final int maxTokens;
    private final AtomicInteger availableTokens;
    private final ScheduledExecutorService scheduler;

    public RateLimiter(int maxTokens, int refillRatePerSecond) {
        this.maxTokens = maxTokens;
        this.availableTokens = new AtomicInteger(maxTokens);
        this.scheduler = new ScheduledThreadPoolExecutor(1);
        scheduleTokenRefill(refillRatePerSecond);
    }

    private void scheduleTokenRefill(int refillRatePerSecond) {
        scheduler.scheduleAtFixedRate(
                () -> availableTokens.set(maxTokens),
                0,
                1000 / refillRatePerSecond,
                TimeUnit.MILLISECONDS
        );
    }

    public CompletableFuture<Void> executeWithRateLimit(Runnable task) {
        return CompletableFuture.supplyAsync(() -> task)
                .thenAcceptAsync(runnable -> {
                    if (availableTokens.get() > 0) {
                        availableTokens.decrementAndGet();
                        runnable.run();
                    } else {
                        throw new RuntimeException("Limit exceeded");
                    }
                });
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(5, 1); // Allow 5 tasks per second
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            rateLimiter.executeWithRateLimit(() ->
                    System.out.println(Thread.currentThread().getName() + " is executing task " + taskId)
            ).exceptionally(throwable -> {
                System.out.println("Task " + taskId + " failed: " + throwable.getMessage());
                return null;
            });
            System.out.println("out");
        }
    }
}