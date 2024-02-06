package com.tasks.completed.letters;

/*
 * Задача
 *     Даны 3 потока, каждый из которых делает запись в "char[] line".
 *     Первый записывает буквы "a", второй буквы "b", третий буквы "c".
 *     В массиве line должны сначала идти все буквы "a", затем "b", затем "c"
 *     независимо от порядка запуска потоков.
 *
 * Реализация
 *     Использование класса Semaphore для ограничения доступа к общему ресурсу.
 * */

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    private static final Semaphore semaphoreA = new Semaphore(1);
    private static final Semaphore semaphoreB = new Semaphore(0);
    private static final Semaphore semaphoreC = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        SharedLine sharedLine = new SharedLine();

        Random random = new Random();

        Thread threadA = new ThreadA(sharedLine, random.nextInt(3, 7));
        Thread threadB = new ThreadB(sharedLine, random.nextInt(10, 20));
        Thread threadC = new ThreadC(sharedLine, random.nextInt(6, 18));

        threadC.start();
        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        threadC.join();

        sharedLine.print();
    }

    private static class SharedLine {
        private char[] buff = new char[8];
        private int index;

        public synchronized void append(char c) {
            if (index >= buff.length) {
                char[] newBuff = new char[buff.length * 2];
                System.arraycopy(buff, 0, newBuff, 0, buff.length);
                buff = newBuff;
            }
            buff[index++] = c;
        }

        public synchronized void print() {
            System.out.print("\nSharedLine: ");
            for (int i = 0; i < index; i++) {
                System.out.print(buff[i]);
            }
            System.out.println();
        }
    }

    private static class ThreadA extends Thread {
        private final SharedLine sharedLine;
        private final int amountOfLetters;

        public ThreadA(SharedLine sharedLine, int amountOfLetters) {
            this.sharedLine = sharedLine;
            this.amountOfLetters = amountOfLetters;
        }

        @Override
        public void run() {
            try {
                semaphoreA.acquire();
                for (int i = 0; i < amountOfLetters; i++) {
                    sharedLine.append('a');
                }
                semaphoreB.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ThreadB extends Thread {
        private final SharedLine sharedLine;
        private final int amountOfLetters;

        public ThreadB(SharedLine sharedLine, int amountOfLetters) {
            this.sharedLine = sharedLine;
            this.amountOfLetters = amountOfLetters;
        }

        @Override
        public void run() {
            try {
                semaphoreB.acquire();
                for (int i = 0; i < amountOfLetters; i++) {
                    sharedLine.append('b');
                }
                semaphoreC.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ThreadC extends Thread {
        private final SharedLine sharedLine;
        private final int amountOfLetters;

        public ThreadC(SharedLine sharedLine, int amountOfLetters) {
            this.sharedLine = sharedLine;
            this.amountOfLetters = amountOfLetters;
        }

        @Override
        public void run() {
            try {
                semaphoreC.acquire();
                for (int i = 0; i < amountOfLetters; i++) {
                    sharedLine.append('c');
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}