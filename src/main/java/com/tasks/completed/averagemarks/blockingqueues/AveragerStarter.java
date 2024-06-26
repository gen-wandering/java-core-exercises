package com.tasks.completed.averagemarks.blockingqueues;

import java.util.ArrayList;
import java.util.List;

public class AveragerStarter {
    private final List<Averager> averagers;
    private final PutTakeQueue<AveragingTask> averagingTaskQueue;

    public AveragerStarter(int amountOfThreads, PutTakeQueue<AveragingTask> averagingTaskQueue) {
        this.averagers = new ArrayList<>(amountOfThreads);
        this.averagingTaskQueue = averagingTaskQueue;

        for (int i = 0; i < amountOfThreads; i++) {
            averagers.add(new Averager(averagingTaskQueue, "executors/fileprocessing/A" + i));
        }
        for (Thread averager : averagers) {
            averager.start();
        }
    }

    public void interrupt() {
        while (!averagingTaskQueue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Averager averager : averagers) {
            averager.interrupt();
        }
    }
}