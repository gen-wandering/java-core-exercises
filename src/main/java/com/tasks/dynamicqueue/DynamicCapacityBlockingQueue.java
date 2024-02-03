package com.tasks.dynamicqueue;

import com.tasks.dynamicqueue.exceptions.*;

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
        if (size() > newMaxCapacity) {
            throw new CapacityReductionException("newMaxCapacity is less than queue size");
        }
        lock.lock();
        try {
            if (newMaxCapacity == maxCapacity) return;
            maxCapacity = newMaxCapacity;
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void add(T item) {
        lock.lock();
        try {
            if (size() == maxCapacity)
                throw new QueueFullException("Queue is full");
        } finally {
            lock.unlock();
        }
        queue.add(item);
    }

    public T remove() {
        if (queue.isEmpty()) throw new QueueEmptyException("Queue is empty");
        else {
            T item = queue.remove();
            lock.lock();
            try {
                notFull.signal();
                return item;
            } finally {
                lock.unlock();
            }
        }
    }

    public void put(T item) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == maxCapacity)
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