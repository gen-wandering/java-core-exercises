package com.tasks.current.completablefuture.ratelimiter;

import com.tasks.current.completablefuture.retrymechanism.RecursiveRetryMechanism;

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
        return RecursiveRetryMechanism.retryTask(maxRetries + 1, retryDilayMillis, rateLimiterTask);
    }
}