package com.tasks.averagemarks.blockingqueues;

import java.util.concurrent.BlockingQueue;

public class Averager extends Thread {
    private final BlockingQueue<AveragingTask> taskQueue;

    public Averager(BlockingQueue<AveragingTask> taskQueue, String name) {
        super(name);
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                AveragingTask task = taskQueue.take();
                double average = calculate(task.marks(), task.startIndex(), task.endIndex());
                System.out.println(Thread.currentThread().getName() + " average: " + average);
            } catch (InterruptedException e) {
                System.out.println("Averager thread " + Thread.currentThread().getName() + " was interrupted");
                return;
            }
        }
    }

    private double calculate(int[] marks, int startIndex, int endIndex) {

        System.out.println(Thread.currentThread().getName() + " start: " + startIndex + "\tend: " + endIndex);

        int sum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += marks[i];
        }
        return (double) sum / marks.length;
    }
}