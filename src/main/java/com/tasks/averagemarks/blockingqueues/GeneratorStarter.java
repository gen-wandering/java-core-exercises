package com.tasks.averagemarks.blockingqueues;

public class GeneratorStarter {
    private final int N;
    private final int amountOfThreads;
    private final PutTakeQueue<AveragingTask> averagingTasksQueue;

    public GeneratorStarter(int N, int amountOfThreads, PutTakeQueue<AveragingTask> averagingTasksQueue) {
        this.N = N;
        this.amountOfThreads = amountOfThreads;
        this.averagingTasksQueue = averagingTasksQueue;
    }

    public void startGeneratingThreads() {
        Splitter splitter = new Splitter(N, amountOfThreads);

        final int maxSubPartsNumber = 10;
        final int minSubPartsNumber = 2;
        int subPartsNumber;

        while (!splitter.isEmpty()) {
            var part = splitter.next();

            if (part.size() > maxSubPartsNumber) subPartsNumber = maxSubPartsNumber;
            else subPartsNumber = minSubPartsNumber;

            new Generator(N, part, subPartsNumber, averagingTasksQueue).start();
        }
    }
}