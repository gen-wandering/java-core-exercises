package com.tasks.completed.averagemarks.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/*
 * Задача
 *     Дан массив "int[] a", выполнить заполнение и для подсчета
 *     среднего значения массива в многопоточном режиме.
 * */

public class Main {
    public static void main(String[] args) {
        final int[] a = new int[10_000];

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var generationAction = new GenerationRecursiveTask(a, 0, a.length);

        var avg = forkJoinPool.invoke(generationAction);

        System.out.println("IntStream avg: " + IntStream.of(a).average());
        System.out.println("ForkJoinPool avg: " + avg);
    }
}