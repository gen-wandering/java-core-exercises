package com.tasks.completed.executors.fileprocessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class CountTask implements Callable<Integer> {
    private final Path filePath;
    private final String targetWord;

    public CountTask(Path filePath, String targetWord) {
        this.filePath = filePath;
        this.targetWord = targetWord;
    }

    @Override
    public Integer call() {

        System.out.println(Thread.currentThread().getName() + " processes file '" + filePath.getFileName() + "'");
        WordCounter wordCounter = new WordCounter();

        try (var reader = Files.newBufferedReader(filePath)) {

            reader.lines().forEach(wordCounter);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return wordCounter.getCount();
    }

    private class WordCounter implements Consumer<String> {
        private int count;
        private final List<String> punctuationMarks = List.of(".", ",", ":", ";", "!", "?");

        public int getCount() {
            return count;
        }

        @Override
        public void accept(String targetLine) {
            for (String word : targetLine.split(" ")) {
                if (word.equals(targetWord)) {
                    count++;
                }
                else if (word.contains(targetWord) && word.length() == targetWord.length() + 1) {
                    String suffix = word.substring(word.lastIndexOf(targetWord) + 1);
                    if (punctuationMarks.contains(suffix)) {
                        count++;
                    }
                }
            }
        }
    }
}
