package com.tasks.averagemarks.blockingqueues;

import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PutTakeQueue<T> {
    private final ArrayDeque<T> queue;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private final int maxSize;

    public PutTakeQueue(int capacity) {
        this.maxSize = capacity;
        this.queue = new ArrayDeque<>(capacity);
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    public void put(T item) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (size() == maxSize)
                notFull.await();
            notEmpty.signal();
            queue.addLast(item);
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            while (size() == 0)
                notEmpty.await();
            notFull.signal();
            return queue.removeFirst();
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return queue.toString();
        } finally {
            lock.unlock();
        }
    }
}