package com.tasks.completed.averagemarks.blockingqueues;

public class Averager extends Thread {
    private final PutTakeQueue<AveragingTask> taskQueue;

    public Averager(PutTakeQueue<AveragingTask> taskQueue, String name) {
        super(name);
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                AveragingTask task = taskQueue.take();
                double average = calculate(task.N(), task.a());
                System.out.println(Thread.currentThread().getName() + " average: " + average);
            } catch (InterruptedException e) {
                System.out.println("Averager thread " + Thread.currentThread().getName() + " was interrupted");
                return;
            }
        }
    }

    private double calculate(int N, int[] a) {
        int sum = 0;
        for (int value : a) {
            sum += value;
        }
        return (double) sum / N;
    }
}