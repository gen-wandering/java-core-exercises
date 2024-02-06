package com.tasks.completed.dynamicqueue;

public class Main {
    private static final DynamicCapacityBlockingQueue<String> dynamicQueue =
            new DynamicCapacityBlockingQueue<>(3);

    public static void main(String[] args) throws InterruptedException {
        showWorkflow();
    }

    private static class FillerThread extends Thread {
        public FillerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    dynamicQueue.put(getName() + "-" + i);
                    System.out.println(getName() + " puts element");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void printQueue() {
        System.out.println("Queue[s: " + dynamicQueue.size() +
                ", rc: " + dynamicQueue.remainingCapacity() + "]: " +
                dynamicQueue
        );
    }

    private static void showWorkflow() throws InterruptedException {
        printQueue();

        new FillerThread("FillerA").start();
        new FillerThread("FillerB").start();
        Thread.sleep(100);

        printQueue();

        System.out.println("Increase capacity to 8");
        dynamicQueue.setMaxCapacity(8);
        printQueue();
        Thread.sleep(100);
        printQueue();

        System.out.println("Take 2 elements");
        Thread.sleep(100);

        System.out.println("Taken element: " + dynamicQueue.take());
        System.out.println("Taken element: " + dynamicQueue.remove());
        Thread.sleep(100);

        printQueue();
    }
}