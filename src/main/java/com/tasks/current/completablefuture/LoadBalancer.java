package com.tasks.current.completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Load balancing:
 *    Implement load balancing strategies using CompletableFuture to
 *    distribute tasks among multiple worker threads or external resources
 *    efficiently, optimizing resource utilization and throughput.
 * */

public class LoadBalancer {
    private final ExecutorService executorService;
    private final List<CompletableFuture<Void>> tasks;

    public LoadBalancer(int numWorkers) {
        // Create a thread pool executor
        executorService = Executors.newFixedThreadPool(numWorkers);
        tasks = new ArrayList<>();
    }

    public void submitTask(Runnable task) {
        // Submit the task to the executor service and add the CompletableFuture to the tasks list
        tasks.add(CompletableFuture.runAsync(task, executorService));
    }

    public CompletableFuture<Void> waitForCompletion() {
        // Combine all CompletableFuture objects into a single CompletableFuture
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new));
        allFutures.join();

        // Return the aggregated CompletableFuture
        return allFutures;
    }

    public void shutdown() {
        // Shutdown the executor service
        executorService.shutdown();
    }

    public static void main(String[] args) {
        // Create a load balancer with 4 worker threads
        LoadBalancer loadBalancer = new LoadBalancer(4);

        // Submit some tasks to the load balancer
        for (int i = 0; i < 10; i++) {
            int taskId = i;
            loadBalancer.submitTask(() -> {
                // Simulate some processing time
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Task " + taskId + " completed by thread " + Thread.currentThread().getName());
            });
        }

        // Wait for all tasks to complete
        CompletableFuture<Void> allTasksCompleted = loadBalancer.waitForCompletion();
        allTasksCompleted.thenRun(() -> {
            System.out.println("All tasks completed.");
            loadBalancer.shutdown();
        });
    }
}