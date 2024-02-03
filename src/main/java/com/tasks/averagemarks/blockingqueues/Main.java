package com.tasks.averagemarks.blockingqueues;

/*
 * Задача
 *     Дан отрезок от [0, N], 2 (или более) потока производят заполнение,
 *     передавая подзадачи подсчета среднего на заполненном участке отрезка
 *     считающим потокам.
 * */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PutTakeQueue<AveragingTask> putTakeQueue = new PutTakeQueue<>(5);

        GeneratorStarter generatorStarter = new GeneratorStarter(10_000, 2, putTakeQueue);
        AveragerStarter averagerStarter = new AveragerStarter(5, putTakeQueue);

        generatorStarter.startGeneratingThreads();

        Thread.sleep(2000);
        averagerStarter.interrupt();
    }
}