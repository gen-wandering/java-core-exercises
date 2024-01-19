package com.tasks.averagemarks.syncindex;

import com.tasks.averagemarks.runnableth.AverageResult;

import java.util.concurrent.ThreadLocalRandom;

public class Marks extends Thread {
    private final int[] marks;
    private final SharedIndex index;
    private final Thread averageThread;

    public Marks(int[] marks, SharedIndex index, SharedIndex averageIndex,
                 AverageResult averageResult, String name) {
        super(name);
        this.marks = marks;
        this.index = index;
        this.averageThread = new Thread(new Average(marks, averageIndex, averageResult));
        if (name.contains("First"))
            averageThread.setName("AverageFirstThread");
        else
            averageThread.setName("AverageSecondThread");
    }

    @Override
    public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = index.getAndIncrement(); i < marks.length; i = index.getAndIncrement()) {
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