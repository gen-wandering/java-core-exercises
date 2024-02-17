package com.tasks.current.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
 *  Event-driven composition:
 *     Design CompletableFuture chains that react to external
 *     events or triggers, dynamically adapting their execution
 *     flow based on incoming events.
 * */

public class EventDrivenComposition {

    public static void main(String[] args) throws InterruptedException {
        // Simulate an external event trigger (e.g., a user action)
        CompletableFuture<Void> externalEvent = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("External event occurred.");
        });

        // CompletableFuture chain reacting to the external event
        CompletableFuture<String> resultFuture = externalEvent.thenCompose(event -> {
            // Perform additional tasks based on the external event
            System.out.println("Reacting to the external event...");

            // Simulate processing time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            // Return the result
            return CompletableFuture.supplyAsync(() -> "Reaction on the external event finished");
        });

        // Await the completion of the CompletableFuture chain
        resultFuture.join();

        // Add a fallback in case the external event does not occur
        CompletableFuture<String> fallbackFuture = CompletableFuture.supplyAsync(() -> {
            // Fallback task
            System.out.println("No external event occurred. Executing fallback task...");
            return "Fallback task completed.";
        });

        // Combine the resultFuture and fallbackFuture to handle the completion of either one
        CompletableFuture<Object> combinedFuture = resultFuture.applyToEither(fallbackFuture, result -> result);

        // Print the result of the completed CompletableFuture
        combinedFuture.thenAccept(res -> System.out.println("Result: " + res));

        // Wait for the result to be printed
        combinedFuture.join();
    }
}