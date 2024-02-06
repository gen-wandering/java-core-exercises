package com.tasks.completed.executors.matrices;

/*
 * Concurrent Matrix Multiplication
 *    Implement a matrix multiplication program that performs the multiplication
 *    of two matrices concurrently using an ExecutorService. Divide the multiplication
 *    task into smaller subtasks and assign them to different threads. Ensure proper
 *    synchronization to aggregate the results accurately.
 * */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static final List<Future<?>> futureList = new ArrayList<>();

    private static final int[][] matrixA = {
            {32, 3, 24, 7, 42, 78},
            {7, 1, -5, 6, 3, 6},
            {-6, 23, 2, 5, 5, 34},
            {2, 5, -13, 4, -2, -1},
            {9, 3, 8, 7, 4, 3},
            {-2, 9, 0, 65, 6, 2}
    };

    private static final int[][] matrixB = {
            {32, 3, 24, 7, 42, 78},
            {7, 1, -5, 6, 3, 6},
            {-6, 23, 2, 5, 5, 34},
            {2, 5, -13, 4, -2, -1},
            {9, 3, 8, 7, 4, 3},
            {-2, 9, 0, 65, 6, 2}
    };

    private static final int[][] resMatrix = new int[matrixA.length][matrixB[0].length];

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                futureList.add(executorService.submit(new MultiplicationTask(i, j)));
            }
        }
        executorService.shutdown();

        for (var future : futureList) {
            future.get();
        }
        if (executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println(Arrays.deepToString(resMatrix));
        }
    }

    private record MultiplicationTask(int i, int j) implements Runnable {
        @Override
        public void run() {
            int res = 0;

            for (int k = 0; k < matrixA.length; k++) {
                res += matrixA[i][k] * matrixB[k][j];
            }
            resMatrix[i][j] = res;
        }
    }
}
