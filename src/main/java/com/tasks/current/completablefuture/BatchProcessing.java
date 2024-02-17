package com.tasks.current.completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Batch processing:
 *    Implement batch processing using CompletableFuture to
 *    process data in chunks or batches asynchronously, improving
 *    throughput and efficiency for tasks involving large datasets.
 * */

public class BatchProcessing {

    public static void main(String[] args) {
        // Sample data
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            data.add(i);
        }

        // Batch size
        int batchSize = 10;

        // Process data in batches asynchronously
        processInBatches(data, batchSize)
                .thenAccept(result -> System.out.println("All batches processed successfully."))
                .exceptionally(throwable -> {
                    System.err.println("Batch processing failed: " + throwable.getMessage());
                    return null;
                });

        // Wait for the result to be printed
        try {
            Thread.sleep(2000); // Wait for 2 seconds for the asynchronous tasks to complete
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public static CompletableFuture<Void> processInBatches(List<Integer> data, int batchSize) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<Void> resultFuture = CompletableFuture.completedFuture(null);

        for (int i = 0; i < data.size(); i += batchSize) {
            List<Integer> batch = data.subList(i, i + batchSize);
            CompletableFuture<Void> batchFuture = CompletableFuture.runAsync(() -> processBatch(batch), executor);

            // Chain the completion of batchFuture with the resultFuture
            resultFuture = resultFuture.thenCombine(batchFuture, (r1, r2) -> null);
        }

        // Shutdown the executor after all tasks are completed
        resultFuture = resultFuture.whenComplete((result, throwable) -> executor.shutdown());

        return resultFuture;
    }

    public static void processBatch(List<Integer> batch) {
        // Simulate processing time
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        // Print the processed batch
        System.out.println("Processed batch: " + batch);
    }
}