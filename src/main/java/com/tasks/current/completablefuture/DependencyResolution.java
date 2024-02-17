package com.tasks.current.completablefuture;

import java.util.concurrent.CompletableFuture;

/*
 * Dependency resolution:
 *    Resolve dependencies between CompletableFuture instances
 *    dynamically by chaining dependent tasks conditionally based
 *    on the completion of other tasks, enabling complex dependency
 *    graphs in asynchronous workflows.
 * */

public class DependencyResolution {

    public static void main(String[] args) {
        // Create CompletableFuture instances representing different tasks
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task 1 is running");
            return 1;
        });

        CompletableFuture<Integer> task2 = task1.thenApply(res1 -> {
            System.out.println("Task 2 is running");
            return 2 + res1;
        });

        CompletableFuture<Integer> task3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task 3 is running");
            return 3;
        });

        CompletableFuture<Integer> task4 = task3.thenCombine(task2, (res2, res3) -> {
            System.out.println("Task 4 is running");
            return 4 + res2 + res3;
        });

        // Resolve dependencies between tasks
        // Task 2 depends on the result of task 1
        // Task 3 is independent of task 1 and task 2
        // Task 4 depends on the result of task 2 and task 3
        // Get the result of the final task
        task4.thenAccept(result -> System.out.println("Final Result: " + result));
    }
}