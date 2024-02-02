package com.tasks.dynamicqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DynamicCapacityBlockingQueue<T> {
    private final BlockingQueue<T> queue;
    private final ReentrantLock lock;
    private int maxCapacity;
    private final Condition notFull;

    public DynamicCapacityBlockingQueue(int initialCapacity) {
        this.queue = new LinkedBlockingQueue<>();
        this.lock = new ReentrantLock();
        this.maxCapacity = initialCapacity;
        this.notFull = lock.newCondition();
    }

    public void setMaxCapacity(int newMaxCapacity) {
        if (maxCapacity >= newMaxCapacity) {
            throw new RuntimeException("maxCapacity >= newMaxCapacity");
        }
        lock.lock();
        try {
            maxCapacity = newMaxCapacity;
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() >= maxCapacity)
                notFull.await();
            queue.put(item);
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        T item = queue.take();
        lock.lock();
        try {
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return queue.size();
    }

    public int remainingCapacity() {
        int size = size();
        lock.lock();
        try {
            return maxCapacity - size;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }
}