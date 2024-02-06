package com.tasks.completed.averagemarks.syncindex;

import com.tasks.completed.averagemarks.runnableth.AverageResult;

import java.util.Arrays;

/*
 * Задача
 *     Дан массив "int[] marks", создать 2 потока для заполнения
 *     массива и 2 потока для подсчета среднего значения. При этом,
 *     как только один из заполняющих потоков закончил работу, должен
 *     запускаться поток для подсчета среднего значения.
 *
 * Реализация
 *     Использование SharedIndex для двух типов потоков и
 *     AverageResult для возврата результата работы потока.
 * */

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final int[] marks = new int[10];

        SharedIndex marksIndex = new SharedIndex();
        SharedIndex averageIndex = new SharedIndex();

        AverageResult firstResult = new AverageResult();
        AverageResult secondResult = new AverageResult();

        Thread first = new Marks(marks, marksIndex, averageIndex, firstResult, "MarksFirstThread");
        Thread second = new Marks(marks, marksIndex, averageIndex, secondResult, "MarksSecondThread");

        first.start();
        second.start();

        first.join();
        second.join();

        System.out.println("marks: " + Arrays.toString(marks));
        System.out.println("Threads average: " + (firstResult.getResult() + secondResult.getResult()));
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