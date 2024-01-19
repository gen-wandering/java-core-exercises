package com.tasks.averagemarks.callableth;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;

public class Marks implements Callable<Double> {
    private final int[] marks;
    private final int startIndex;
    private final FutureTask<Double> averageTask;
    private final Thread averageThread;

    public Marks(int[] marks, int startIndex) {
        this.marks = marks;
        this.startIndex = startIndex;
        this.averageTask = new FutureTask<>(new Average(marks, startIndex));
        this.averageThread = new Thread(averageTask);
        if (startIndex == 0)
            averageThread.setName("AverageThreadEven");
        else
            averageThread.setName("AverageThreadOdd");
    }

    @Override
    public Double call() throws ExecutionException, InterruptedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = startIndex; i < marks.length; i += 2) {
            marks[i] = random.nextInt(1, 101);
            System.out.println(Thread.currentThread().getName() + "\t mark[" + i + "] entered: " + marks[i]);
        }
        averageThread.start();
        return averageTask.get();
    }
}