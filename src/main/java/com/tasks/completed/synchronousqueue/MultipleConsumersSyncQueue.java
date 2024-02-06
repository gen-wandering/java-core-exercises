package com.tasks.completed.synchronousqueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class MultipleConsumersSyncQueue {
    private final static BlockingQueue<Integer> queue = new SynchronousQueue<>();

    public static void main(String[] args) {
        List<Thread> consumers = new ArrayList<>();
        ProducerThread producerThread = new ProducerThread();

        for (int consumerId = 1; consumerId <= 3; consumerId++) {
            consumers.add(new ConsumerThread("Consumer-" + consumerId));
        }
        consumers.forEach(Thread::start);
        producerThread.start();

        try {
            producerThread.join();
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        System.out.println("Producer thread: " + producerThread.getState());
        consumers.forEach(thread -> System.out.println(thread.getName() + ": " + thread.getState()));
    }

    private static class ProducerThread extends Thread {
        @Override
        public void run() {
            try {
                queue.put(312);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ConsumerThread extends Thread {
        public ConsumerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                Integer res = queue.take();
                System.out.println(getName() + " takes " + res + " form the queue");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
