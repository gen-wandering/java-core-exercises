package com.tasks.averagemarks.blockingqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class GeneratorStarter {
    private final int[] marks;
    private final int partsNumber;
    private final BlockingQueue<AveragingTask> averagingTasksQueue;
    private final BlockingQueue<GenerationTask> generationTaskQueue;
    private final List<Generator> generators;

    public GeneratorStarter(int[] marks, int partsNumber, int amountOfThreads,
                            BlockingQueue<GenerationTask> generationTaskQueue,
                            BlockingQueue<AveragingTask> averagingTasksQueue) {
        this.marks = marks;
        this.partsNumber = partsNumber;
        this.averagingTasksQueue = averagingTasksQueue;

        this.generationTaskQueue = generationTaskQueue;
        this.generators = new ArrayList<>(amountOfThreads);

        for (int i = 0; i < amountOfThreads; i++) {
            generators.add(new Generator(generationTaskQueue, "G" + i));
        }
        for (Generator generator : generators) {
            generator.start();
        }
    }

    public void splitAndEnqueue() {
        if (partsNumber < 1 || marks.length < partsNumber) {
            throw new RuntimeException();
        }
        int partSize = marks.length / partsNumber;
        int remainder = marks.length % partsNumber;

        for (int i = 0; i < partsNumber; i++) {
            int start = i * partSize + Math.min(i, remainder);
            int end = i == (partsNumber - 1) ? marks.length : (i + 1) * partSize + Math.min(i + 1, remainder);

            try {
                generationTaskQueue.put(new GenerationTask(marks, start, end - 1, averagingTasksQueue));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void interrupt() {
        while (!generationTaskQueue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Generator generator : generators) {
            generator.interrupt();
        }
    }
}