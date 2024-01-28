package com.tasks.averagemarks.blockingqueues;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Generator extends Thread {
    private final BlockingQueue<GenerationTask> generationTasksQueue;

    public Generator(BlockingQueue<GenerationTask> generationTasksQueue, String name) {
        super(name);
        this.generationTasksQueue = generationTasksQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                GenerationTask task = generationTasksQueue.take();
                generateAndEnqueue(task.marks(), task.startIndex(), task.endIndex(), task.averagingTasksQueue());
            } catch (InterruptedException e) {
                System.out.println("Generator thread " + Thread.currentThread().getName() + " was interrupted");
                return;
            }
        }
    }

    private void generateAndEnqueue(int[] marks, int startIndex, int endIndex, BlockingQueue<AveragingTask> averageTaskQueue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        System.out.println(Thread.currentThread().getName() + " start: " + startIndex + "\tend: " + endIndex);

        for (int i = startIndex; i <= endIndex; i++) {
            marks[i] = random.nextInt(1, 10001);
        }
        try {
            averageTaskQueue.put(new AveragingTask(marks, startIndex, endIndex));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
