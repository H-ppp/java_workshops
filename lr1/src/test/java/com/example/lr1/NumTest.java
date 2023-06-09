package com.example.lr1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.lr1.cache.Cache;
import com.example.lr1.count.CountThread;
import com.example.lr1.exception.IllegalArguments;
import com.example.lr1.service.NumberServ;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class NumTest {
    NumControl numControl = new NumControl(new Cache<Integer, ArrayList <Integer>>(), new CountThread(), new NumberServ(null));

    @Test
    void isGenNumbersRight() throws IllegalArgumentException, IllegalArguments, InterruptedException {

        ArrayList<Integer> testList = numControl.showRandList(13);
        for (Integer i = 0; i < 5; i++) {
            assertTrue(testList.get(i) <= 13);
        }
    }

    @Test
    void ifLessThanFive(){
        Assertions.assertThrows(IllegalArguments.class, ()->{
            numControl.showRandList(2);
        });
    }

    @Test
    void ifGreaterThanHundred(){
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            numControl.showRandList(200);
        });
    }
}