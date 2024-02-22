package com.tasks.current.completablefuture.ratelimiter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RefillRateScheduler {
    private static boolean isInitialized = false;
    private static int maxTokens = 0;
    private static final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    // amount of tasks, that can be executed before next refill
    public static AtomicInteger availableTokens = new AtomicInteger();

    public static void scheduleTokenRefill(int maxTokens, int refillRatePerSecond) {
        if (RefillRateScheduler.maxTokens == 0 && !isInitialized) {
            isInitialized = true;
            RefillRateScheduler.maxTokens = maxTokens;

            scheduler.scheduleAtFixedRate(
                    () -> availableTokens.set(maxTokens),
                    0,
                    1000 / refillRatePerSecond,
                    TimeUnit.MILLISECONDS
            );
        }
    }
}