package com.tasks.synchronousqueue;

import java.util.concurrent.*;

public class MultipleConsumersExecutors {
    private final static BlockingQueue<Integer> queue = new SynchronousQueue<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(new ProducerTask());
        for (int i = 0; i < 3; i++) {
            executorService.execute(new ConsumerTask());
        }
        executorService.shutdown();
    }

    private static class ProducerTask implements Runnable {
        @Override
        public void run() {
            try {
                queue.put(312);
                System.out.println(Thread.currentThread().getName() + " puts " + 321 + " into the queue");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ConsumerTask implements Runnable {
        @Override
        public void run() {
            try {
                Integer res = queue.take();
                System.out.println(Thread.currentThread().getName() + " takes " + res + " form the queue");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
