package com.example.lr1;
import com.example.lr1.count.CountThread;
import com.example.lr1.count.Counter;
import com.example.lr1.exception.IllegalArguments;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import java.util.Collections;


import java.util.OptionalDouble;
import java.util.stream.Collectors;


public class RandCalculate {
   
    private static final Logger LOGGER = LogManager.getLogger();
    private Cache<Integer, ArrayList<Integer>> cache;
    private CountThread countTread;
    public RandCalculate(Cache<Integer, ArrayList<Integer>> cache, CountThread countTread) {
        this.cache = cache;
        this.countTread = countTread;
    }
    
    public ArrayList<Integer> generateNums(Integer num)
            throws IllegalArgumentException, IllegalArguments {
        countTread.start();

        LOGGER.info("Incoming number: " + num);
        if (num < 5) {
            LOGGER.info("Entered number shall not be less than 5. Your number: " + num);
            LOGGER.info("Requests: " + Counter.getCountVal());
            throw new IllegalArguments("Entered number must not be less than 5");
        }
        if (num > 100) {
            LOGGER.error("Entered number must not be greater than 100");
            LOGGER.info("Requests: " + Counter.getCountVal());
            throw new IllegalArgumentException("Entered number must not be greater than 100");
        }
        ArrayList<Integer> randVals = new ArrayList<Integer>();
        ArrayList<Integer> lambdaRandVals = new ArrayList<Integer>();

        if (cache.contain(num)) {
            LOGGER.info("Using cache");
            LOGGER.info("Requests: " + Counter.getCountVal());
            return cache.getFromCache(num);
        } else {
            for (int i = 1; i <= 5; i++) {
                int yourNumber = (int) (Math.random() * (num + 1));
                LOGGER.info("Generated value: " + yourNumber);
                randVals.add(yourNumber);
            }
            
            lambdaRandVals = randVals;
            lambdaRandVals.stream().map(x -> x + 1);
            int minVal = lambdaRandVals.stream().min(Integer::compareTo).get();
            LOGGER.info("Min value: " + minVal);
            int maxVal = lambdaRandVals.stream().max(Integer::compareTo).get();
            LOGGER.info("Max value: " + maxVal);
            OptionalDouble averVal = lambdaRandVals.stream().mapToInt(n -> n).average();
            LOGGER.info("Average value: " + averVal);
            Collections.sort(lambdaRandVals, (o1, o2) -> (o1 < o2) ? -1 : (o1 > o2) ? 1 : 0);
            Collectors.toList();
            randVals = lambdaRandVals;
            
            cache.saveInCache(num, randVals);

        }
        LOGGER.info("Requests: " + Counter.getCountVal());

        return randVals;
    }

}
