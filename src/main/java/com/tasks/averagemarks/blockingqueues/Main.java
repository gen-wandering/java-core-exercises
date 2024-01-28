package com.tasks.averagemarks.blockingqueues;

/*
 * Задача
 *     Дан массив "int[] marks", 2 (или более) потока заполняют массив,
 *     как только одна из секций массива заполнена поток передает
 *     задачу подсчета среднего значения в данной секции считающим потокам.
 * */

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        final int[] marks = new int[10_000];

        BlockingQueue<GenerationTask> generationTaskQueue = new ArrayBlockingQueue<>(16);
        BlockingQueue<AveragingTask> averagingTaskQueue = new ArrayBlockingQueue<>(32);

        AveragerStarter averagerStarter = new AveragerStarter(5, averagingTaskQueue);
        GeneratorStarter generatorStarter = new GeneratorStarter(marks,
                100, 2, generationTaskQueue, averagingTaskQueue);

        generatorStarter.splitAndEnqueue();

        generatorStarter.interrupt();
        averagerStarter.interrupt();
    }
}