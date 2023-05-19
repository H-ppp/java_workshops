package com.example.lr1.count;

public class Counter {
    private static int counter = 0;

     public static int getCountVal() {
        return counter;
    }

     public static synchronized void increase() {
        ++counter;
    }
}
