package com.example.lr1.count;

import org.springframework.stereotype.Component;

@Component
public class CountThread extends Thread {
    public CountThread() {
        super();
    }

    @Override
    public void start() {
        Counter.increase();
    }
}
