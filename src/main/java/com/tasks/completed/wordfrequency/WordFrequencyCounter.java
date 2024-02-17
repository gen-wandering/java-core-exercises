package com.tasks.completed.wordfrequency;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WordFrequencyCounter {
    static final String trailingPunctuationMarks = "-^*,:;.!?";

    private final Map<String, Integer> words = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public Map<String, Integer> countFrequency(String file) {
        long initTime = System.currentTimeMillis();

        try (var lines = Files.lines(Path.of(file))) {

            lines.forEach(line -> executorService.execute(new CountFrequencyTask(line, words)));
            executorService.shutdown();

            if (executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("WordFrequencyCounter time: " + (System.currentTimeMillis() - initTime));
                return words;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return words;
    }

    private record CountFrequencyTask(String line,
                                      Map<String, Integer> words) implements Runnable {
        @Override
        public void run() {
            for (String word : line.trim().split("\\s+")) {
                if (word.isEmpty()) continue;
                if (trailingPunctuationMarks.contains(word.substring(word.length() - 1))) {
                    word = word.substring(0, word.length() - 1);
                }
                words.merge(word.toLowerCase(), 1, Integer::sum);
            }
        }
    }
}