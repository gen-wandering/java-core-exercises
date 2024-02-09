package com.tasks.current.wordfrequency;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tasks.current.wordfrequency.WordFrequencyCounter.trailingPunctuationMarks;

/*
 * Parallel Word Frequency Counter:
 *    Create a program that reads a large text file, breaks it into words,
 *    and uses a ConcurrentHashMap to count the frequency of each word in parallel.
 * */

public class Benchmark {
    private static int position;

    public static void main(String[] args) throws IOException {
        final String shakespeareX6 = "src/main/resources/wordfrequency/shakespeareX6.txt";

        // AVG-Time (4 worker-threads): 1005.6ms
        WordFrequencyCounter counter = new WordFrequencyCounter();

        var words = counter.countFrequency(shakespeareX6);
        printRes(words);
    }

    // AVG-Time: 1581.4ms
    private static Map<String, Integer> countFrequencySequential(String file) throws IOException {
        Map<String, Integer> words = new HashMap<>();
        long initTime = System.currentTimeMillis();

        try (Scanner scanner = new Scanner(Path.of(file))) {
            while (scanner.hasNext()) {
                var word = scanner.next();

                if (trailingPunctuationMarks.contains(word.substring(word.length() - 1))) {
                    word = word.substring(0, word.length() - 1);
                }
                words.merge(word.toLowerCase(), 1, Integer::sum);
            }
            System.out.println("countFrequencySequential() time: " + (System.currentTimeMillis() - initTime));

            return words;
        }
    }

    // AVG-Time: 1184.2ms
    private static Map<String, Integer> countFrequencyParallel(String file) throws IOException {
        Map<String, Integer> words = new ConcurrentHashMap<>();
        long initTime = System.currentTimeMillis();

        try (var scanner = new Scanner(Path.of(file))) {
            scanner.tokens()
                    .parallel()
                    .forEach(word -> {
                        if (trailingPunctuationMarks.contains(word.substring(word.length() - 1))) {
                            word = word.substring(0, word.length() - 1);
                        }
                        words.merge(word.toLowerCase(), 1, Integer::sum);
                    });
        }
        System.out.println("countFrequencyParallel() time: " + (System.currentTimeMillis() - initTime));

        return words;
    }

    private static void printRes(Map<String, Integer> words) {
        words.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(200)
                .forEach(entry -> {
                    position++;
                    System.out.println("[" + position + "] " + entry.getKey() + "=" + entry.getValue());
                });
        position = 0;
    }
}