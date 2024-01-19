package com.tasks.averagemarks.syncindex;

public class SharedIndex {
    private int i;

    public synchronized int getAndIncrement() {
        return i++;
    }
}
