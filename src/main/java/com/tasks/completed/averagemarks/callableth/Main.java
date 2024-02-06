package com.tasks.completed.averagemarks.callableth;

/*
 * Задача
 *     Дан массив "int[] marks", создать 2 потока для заполнения
 *     массива и 2 потока для подсчета среднего значения. При этом,
 *     как только один из заполняющих потоков закончил работу, должен
 *     запускаться поток для подсчета среднего значения.
 *
 * Реализация
 *     Разделение массива на непересекающиеся области и использование
 *     Callable и FutureTask для возврата результата работы потока.
 * */

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final int[] marks = new int[10];

        FutureTask<Double> futureTaskEven = new FutureTask<>(new Marks(marks, 0));
        FutureTask<Double> futureTaskOdd = new FutureTask<>(new Marks(marks, 1));

        Thread marksThreadEven = new Thread(futureTaskEven, "MarksThreadEven");
        Thread marksThreadOdd = new Thread(futureTaskOdd, "MarksThreadOdd");

        marksThreadEven.start();
        marksThreadOdd.start();

        marksThreadEven.join();
        marksThreadOdd.join();

        System.out.println("marks: " + Arrays.toString(marks));
        System.out.println("Threads average: " + (futureTaskEven.get() + futureTaskOdd.get()));
        System.out.println("Average: " + getAverage(marks));
    }

    private static double getAverage(int[] marks) {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return (double) sum / marks.length;
    }
}