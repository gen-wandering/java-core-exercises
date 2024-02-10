package com.tasks.current.textsearch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
 *  Parallelized Text Search:
 *     Create a parallelized text search engine using ForkJoinPool to search
 *     for a pattern or keyword in large text documents concurrently.
 * */

public class Main {
    public static void main(String[] args) throws Exception {
        String directoryPath = "src/main/resources/executors/fileprocessing";
        String pattern = "take";

        List<Path> filePaths = getFilePaths(directoryPath);
        List<String> lines = readLinesFromFiles(filePaths);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        TextSearchTask searchTask = new TextSearchTask(pattern, lines);
        List<String> matchingLines = forkJoinPool.invoke(searchTask);

        forkJoinPool.shutdown();

        System.out.println("Matching lines:");
        for (String line : matchingLines) {
            System.out.println(line);
        }
    }

    private static List<Path> getFilePaths(String directoryPath) throws Exception {
        try (var walkStream = Files.walk(Path.of(directoryPath))) {
            return walkStream.filter(Files::isRegularFile).toList();
        }
    }

    private static List<String> readLinesFromFiles(List<Path> filePaths) throws Exception {
        List<String> lines = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Callable<List<String>>> tasks = new ArrayList<>();
        filePaths.forEach(path -> tasks.add(new Reader(path)));

        for (var fileLines : executorService.invokeAll(tasks)) {
            lines.addAll(fileLines.get());
        }
        executorService.shutdown();

        return lines;
    }

    private record Reader(Path file) implements Callable<List<String>> {
        @Override
        public List<String> call() throws Exception {
            try (var lines = Files.lines(file)) {
                return lines.toList();
            }
        }
    }
}