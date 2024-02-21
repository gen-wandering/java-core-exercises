package com.tasks.current.completablefuture.ratelimiter;

import com.tasks.current.completablefuture.retrymechanism.Task;

public record RateLimiterTask(int id) implements Task<String> {

    @Override
    public String execute() throws Exception {
        if (RateScheduler.availableTokens.getAndDecrement() > 0) {
            System.out.println(Thread.currentThread().getName() + " is executing Task [" + id + "]");
            return "Task[" + id + "] completed";
        } else {
            throw new Exception("Limit exceeded");
        }
    }
}
