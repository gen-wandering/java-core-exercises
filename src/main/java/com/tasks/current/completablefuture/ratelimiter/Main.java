package com.tasks.current.completablefuture.ratelimiter;

/*
 * Rate limiting with retry mechanism:
 *    Create a rate-limiting mechanism using CompletableFuture
 *    to control the rate at which tasks are executed, ensuring
 *    they do not exceed a certain limit per unit of time.
 *
 *    If some tasks were not completed due to rate limiting,
 *    they should be sent to the retry mechanism.
 * */

public class Main {
    // max amount of tasks per second
    private static final int MAX_TOKENS = 5;

    // token refresh rate per second
    private static final int REFILL_PER_SECOND = 1;

    // if some task exceeded the limit, then
    // retry mechanism will try to perform this
    // task MAX_RETRIES times
    private static final int MAX_RETRIES = 3;

    // time interval between retries for the same task
    private static final int RETRY_DILAY_MILLIS = 500;

    public static void main(String[] args) throws InterruptedException {

        // 5 tasks per second with 3 possible retries for each
        var rateLimiter = new RateLimiterWithRetry(
                MAX_TOKENS, REFILL_PER_SECOND,
                MAX_RETRIES, RETRY_DILAY_MILLIS
        );

        for (int i = 0; i < 10; i++) {
            rateLimiter.executeWithRateLimit(new RateLimiterTask(i))
                    .thenAcceptAsync(System.out::println)
                    .exceptionally(throwable -> {
                        System.out.println(throwable.getMessage());
                        return null;
                    });
        }
    }
}
