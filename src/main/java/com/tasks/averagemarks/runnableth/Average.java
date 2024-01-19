package com.tasks.averagemarks.runnableth;

public class Average implements Runnable {
    private final int[] marks;
    private final int startIndex;
    private final AverageResult averageResult;

    public Average(int[] marks, int startIndex, AverageResult averageResult) {
        this.marks = marks;
        this.startIndex = startIndex;
        this.averageResult = averageResult;
    }

    @Override
    public void run() {
        int sum = 0;
        for (int i = startIndex; i < marks.length; i += 2) {
            sum += marks[i];
            System.out.println(Thread.currentThread().getName() +
                    "\t mark[" + i + "] added: " + marks[i] + ", sum: " + sum);
        }
        averageResult.setResult((double) sum / marks.length);
    }
}