package com.tasks.current.completablefuture.ratelimiter;

public record RateLimiterTask(int id) {
    public String execute() {
        System.out.println(Thread.currentThread().getName() + " is executing Task [" + id + "]");
        return "Task[" + id + "] completed";
    }
}
