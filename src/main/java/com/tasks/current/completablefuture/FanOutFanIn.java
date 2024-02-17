package com.tasks.current.completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FanOutFanIn {

    public static void main(String[] args) throws InterruptedException {
        // Create some sample data
        List<Integer> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            data.add(i);
        }

        // Fan-out/fan-in processing
        CompletableFuture<List<String>> resultFuture = fanOutFanInProcessing(data);

        // Handle the aggregated result
        resultFuture.thenAccept(result -> {
            System.out.println("Aggregated Result:");
            result.forEach(System.out::println);
        });

        // Await termination to see the result printed
        Thread.sleep(2000);
    }

    public static CompletableFuture<List<String>> fanOutFanInProcessing(List<Integer> data) {
        // Define the number of threads for fan-out processing
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Fan-out: Create a CompletableFuture for each element in the data list
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (Integer item : data) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> processItem(item), executorService);
            futures.add(future);
        }

        // Fan-in: Aggregate the results of all CompletableFuture objects
        // Combine the results of all CompletableFuture objects into a single CompletableFuture
        CompletableFuture<List<String>> resultFuture = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());

        executorService.shutdown();

        return resultFuture;
    }

    public static String processItem(int item) {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return "Processed item: " + item;
    }
}