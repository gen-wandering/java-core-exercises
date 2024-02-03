package com.tasks.averagemarks.blockingqueues;

import java.util.ArrayList;
import java.util.List;

public class Splitter {
    private int current;
    private final List<Part> parts;

    public Splitter(int N, int partsNumber) {
        if (partsNumber < 1 || N < partsNumber) {
            throw new RuntimeException("Splitter exception");
        }
        this.parts = new ArrayList<>(partsNumber);

        int partSize = N / partsNumber;
        int remainder = N % partsNumber;

        for (int i = 0; i < partsNumber; i++) {
            int start = i * partSize + Math.min(i, remainder);
            int end = i == (partsNumber - 1) ? N : (i + 1) * partSize + Math.min(i + 1, remainder);

            parts.add(new Part(start, end - 1));
        }
    }

    public boolean isEmpty() {
        return current >= parts.size();
    }

    public Part next() {
        return parts.get(current++);
    }

    public record Part(int start, int end) {
        public int size() {
            return end - start + 1;
        }
    }
}