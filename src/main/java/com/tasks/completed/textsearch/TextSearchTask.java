package com.tasks.completed.textsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class TextSearchTask extends RecursiveTask<List<String>> {
    private static final int THRESHOLD = 10;

    private final String pattern;
    private final List<String> lines;

    public TextSearchTask(String pattern, List<String> lines) {
        this.pattern = pattern;
        this.lines = lines;
    }

    @Override
    protected List<String> compute() {
        if (lines.size() <= THRESHOLD) {
            return lines.stream().filter(s -> s.contains(pattern)).toList();
        } else {
            List<String> res = new ArrayList<>();

            int mid = lines.size() / 2 + lines.size() % 2;
            var left = new TextSearchTask(pattern, lines.subList(0, mid));
            var right = new TextSearchTask(pattern, lines.subList(mid, lines.size()));

            left.fork();
            right.fork();

            res.addAll(left.join());
            res.addAll(right.join());

            return res;
        }
    }
}