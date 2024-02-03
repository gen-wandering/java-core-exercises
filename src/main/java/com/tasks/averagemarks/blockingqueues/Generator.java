package com.tasks.averagemarks.blockingqueues;

import java.util.concurrent.ThreadLocalRandom;

public class Generator extends Thread {
    private final int N;
    private final Splitter.Part part;
    private final int subPartsNumber;
    private final PutTakeQueue<AveragingTask> averageTaskQueue;

    public Generator(int N, Splitter.Part part, int subPartsNumber,
                     PutTakeQueue<AveragingTask> averageTaskQueue) {
        this.N = N;
        this.part = part;
        this.subPartsNumber = subPartsNumber;
        this.averageTaskQueue = averageTaskQueue;
    }

    @Override
    public void run() {
        Splitter splitter = new Splitter(part.size(), subPartsNumber);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        System.out.println(Thread.currentThread().getName() + " start: " + part.start() + "\tend: " + part.end());

        int k = 0;
        while (!splitter.isEmpty()) {
            int[] a = new int[splitter.next().size()];

            for (int i = 0; i < a.length; i++) {
                a[i] = random.nextInt(1, 1001);
            }
            System.out.println(Thread.currentThread().getName() + " enqueue task " + (++k));

            try {
                averageTaskQueue.put(new AveragingTask(N, a));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}