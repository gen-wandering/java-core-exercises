package com.tasks.averagemarks.callableth;

import java.util.concurrent.Callable;

public class Average implements Callable<Double> {
    private final int[] marks;
    private final int startIndex;

    public Average(int[] marks, int startIndex) {
        this.marks = marks;
        this.startIndex = startIndex;
    }

    @Override
    public Double call() {
        int sum = 0;
        for (int i = startIndex; i < marks.length; i += 2) {
            sum += marks[i];
            System.out.println(Thread.currentThread().getName() +
                    "\t mark[" + i + "] added: " + marks[i] + ", sum: " + sum);
        }
        return (double) sum / marks.length;
    }
}