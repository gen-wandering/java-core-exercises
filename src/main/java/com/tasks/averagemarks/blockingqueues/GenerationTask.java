package com.tasks.averagemarks.blockingqueues;

import java.util.concurrent.BlockingQueue;

public record GenerationTask(int[] marks, int startIndex, int endIndex,
                             BlockingQueue<AveragingTask> averagingTasksQueue) {
}
