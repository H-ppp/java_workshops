package com.example.lr1.Counter;

import org.springframework.stereotype.Component;

@Component
public class CountThread extends Thread {
    public CountThread(){
        super();
    }
   
    public void countRequest(){
        Counter.increase();
    }
}
