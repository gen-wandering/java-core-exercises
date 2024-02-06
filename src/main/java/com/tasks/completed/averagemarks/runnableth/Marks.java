package com.tasks.completed.averagemarks.runnableth;

import java.util.concurrent.ThreadLocalRandom;

public class Marks implements Runnable {
    private final int[] marks;
    private final int startIndex;
    private final Thread averageThread;

    public Marks(int[] marks, int startIndex, AverageResult averageResult) {
        this.marks = marks;
        this.startIndex = startIndex;
        this.averageThread = new Thread(new Average(marks, startIndex, averageResult));
        if (startIndex == 0)
            averageThread.setName("AverageThreadEven");
        else
            averageThread.setName("AverageThreadOdd");
    }

    @Override
    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = startIndex; i < marks.length; i += 2) {
            marks[i] = random.nextInt(1, 101);
            System.out.println(Thread.currentThread().getName() + "\t mark[" + i + "] entered: " + marks[i]);
        }
        averageThread.start();
        try {
            averageThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}