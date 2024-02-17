package com.tasks.completed.averagemarks.forkjoin;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;

public class GenerationRecursiveTask extends RecursiveTask<Double> {
    private final static int PARTS = 25;

    private final int[] a;
    private final int start;
    private final int end;

    public GenerationRecursiveTask(int[] a, int start, int end) {
        this.a = a;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Double compute() {
        if (end - start + 1 <= a.length / PARTS) {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            System.out.println("-----" + Thread.currentThread().getName() + ": \tG " + "[" + start + "; " + end + ")");

            for (int i = start; i < end; i++) {
                a[i] = random.nextInt(0, 10_000);
            }
            return new AveragingRecursiveTask(a, start, end).fork().join();
        } else {
            int mid = (start + end) / 2;
            var left = new GenerationRecursiveTask(a, start, mid);
            var right = new GenerationRecursiveTask(a, mid, end);

            left.fork();
            right.fork();

            return left.join() + right.join();
        }
    }
}