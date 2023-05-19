package com.example.lr1.logicOfComputing;

import com.example.lr1.exception.IllegalArguments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import java.util.Collections;

import java.util.stream.Collectors;

public class RandCalculate {

    private static final Logger LOGGER = LogManager.getLogger();

    public RandCalculate() {
    };

    public ArrayList<Integer> generateNums(Integer num)
            throws IllegalArgumentException, IllegalArguments {

        if (num < 5) {
            LOGGER.info("Entered number shall not be less than 5. Your number: " + num);
            throw new IllegalArguments("Entered number must not be less than 5");
        }
        if (num > 100) {
            LOGGER.error("Entered number must not be greater than 100");
            throw new IllegalArgumentException("Entered number must not be greater than 100");
        }

        ArrayList<Integer> randVals = new ArrayList<Integer>();
        ArrayList<Integer> lambdaRandVals = new ArrayList<Integer>();

        for (int i = 1; i <= 5; i++) {
            int yourNumber = (int) (Math.random() * (num + 1));
            randVals.add(yourNumber);

        }

        lambdaRandVals = randVals;
        lambdaRandVals.stream().map(x -> x + 1);
        Collections.sort(lambdaRandVals, (o1, o2) -> (o1 < o2) ? -1 : (o1 > o2) ? 1 : 0);
        Collectors.toList();
        randVals = lambdaRandVals;

        return randVals;
    }

}
