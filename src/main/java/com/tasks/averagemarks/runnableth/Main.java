package com.tasks.averagemarks.runnableth;

import java.util.Arrays;

/*
 * Задача
 *     Дан массив "int[] marks", создать 2 потока для заполнения
 *     массива и 2 потока для подсчета среднего значения. При этом,
 *     как только один из заполняющих потоков закончил работу, должен
 *     запускаться поток для подсчета среднего значения.
 *
 * Реализация
 *     Разделение массива на непересекающиеся области и использование
 *     AverageResult для возврата результата работы потока.
 * */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int[] marks = new int[10];

        AverageResult evenResult = new AverageResult();
        AverageResult oddResult = new AverageResult();

        Thread marksThreadEven = new Thread(new Marks(marks, 0, evenResult), "MarksThreadEven");
        Thread marksThreadOdd = new Thread(new Marks(marks, 1, oddResult), "MarksThreadOdd");

        marksThreadEven.start();
        marksThreadOdd.start();

        marksThreadEven.join();
        marksThreadOdd.join();

        System.out.println("marks: " + Arrays.toString(marks));
        System.out.println("Threads average: " + (evenResult.getResult() + oddResult.getResult()));
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
