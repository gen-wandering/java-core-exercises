package com.tasks.completed.executors.fileprocessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Parallel File Processing:
 *    Develop a program that reads multiple files concurrently using an ExecutorService.
 *    Each file should be processed by a separate thread. Implement a task that counts
 *    the occurrences of specific words in each file and produces a report on the total
 *    word count.
 * */

public class Main {
    public static void main(String[] args) {
        String targetWord = "the";

        ParallelFileProcessor processor = new ParallelFileProcessor(targetWord);
        processor.process();

        System.out.println("Total amount of words '" + targetWord + "': " + processor.getResult());
    }

    private static class ParallelFileProcessor {
        private final Path start = Path.of("src/main/resources/executors/fileprocessing");

        private final ExecutorService executorService;
        private final Map<String, Future<Integer>> futureMap;

        private int result;
        private final String targetWord;

        private ParallelFileProcessor(String targetWord) {
            this.targetWord = targetWord;

            this.executorService = Executors.newFixedThreadPool(3);
            this.futureMap = new HashMap<>();
        }

        public int getResult() {
            return result;
        }

        private void process() {
            try (var stream = Files.walk(start)) {

                stream.forEach(path -> {
                    if (Files.isRegularFile(path))
                        futureMap.put(path.toString(), executorService.submit(new CountTask(path, targetWord)));
                });
                executorService.shutdown();

                for (var entry : futureMap.entrySet()) {
                    String s = entry.getKey();
                    int currentRes = entry.getValue().get();

                    System.out.println("File: " + s.substring(s.lastIndexOf("\\") + 1) +
                            " contains " + currentRes + " words '" + targetWord + "'"
                    );
                    result += currentRes;
                }
            } catch (IOException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
