package com.example.lr1.Counter;

public class Counter {
    private static int counter = 0;

    synchronized public static int getCountVal() {
        return counter;
    }

    synchronized public static void increase() {
        ++counter;
    }
}
