package com.tasks.averagemarks.syncindex;

import com.tasks.averagemarks.runnableth.AverageResult;

public class Average implements Runnable {
    private final int[] marks;
    private final SharedIndex index;
    private final AverageResult averageResult;

    public Average(int[] marks, SharedIndex index, AverageResult averageResult) {
        this.marks = marks;
        this.index = index;
        this.averageResult = averageResult;
    }

    @Override
    public void run() {
        int sum = 0;
        for (int i = index.getAndIncrement(); i < marks.length; i = index.getAndIncrement()) {
            sum += marks[i];
            System.out.println(Thread.currentThread().getName() +
                    "\t mark[" + i + "] added: " + marks[i] + ", sum: " + sum);
        }
        averageResult.setResult((double) sum / marks.length);
    }
}