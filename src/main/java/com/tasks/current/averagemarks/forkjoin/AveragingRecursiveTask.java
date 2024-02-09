package com.tasks.current.averagemarks.forkjoin;

import java.util.concurrent.RecursiveTask;

public class AveragingRecursiveTask extends RecursiveTask<Double> {
    private static final int THRESHOLD = 25;

    private final int[] a;
    private final int start;
    private final int end;

    public AveragingRecursiveTask(int[] a, int start, int end) {
        this.a = a;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - start + 1 <= THRESHOLD) {

            System.out.println(Thread.currentThread().getName() + ": \tA " + "[" + start + "; " + end + ")");

            int sum = 0;
            for (int i = start; i < end; i++) {
                sum += a[i];
            }
            return (double) sum / a.length;
        } else {
            int mid = (start + end) / 2;
            var left = new AveragingRecursiveTask(a, start, mid);
            var right = new AveragingRecursiveTask(a, mid, end);

            left.fork();
            right.fork();

            return left.join() + right.join();
        }
    }
}