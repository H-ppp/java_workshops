package com.example.lr1;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Map;

public class NumTest {
    @Test
    void validateException() {
        NumControl numControl = new NumControl(new Cache<Integer, Map<Integer, Integer>>());
        Map<Integer, Integer> testMap = numControl.showRandList(13);
        // System.out.println(testMap.toString());
        for (Integer i = 1; i <= 5; i++) {
            assertTrue(testMap.get(i) <= 13);
        }
    }
}