package com.example.lr1;

import org.springframework.web.bind.annotation.RestController;

import com.example.lr1.count.CountThread;
import com.example.lr1.count.Counter;
import com.example.lr1.exception.IllegalArguments;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Validated
@RestController
public class NumControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Cache<Integer, ArrayList<Integer>> cache;
    private CountThread countTread;

    // @Autowired
    public NumControl(Cache<Integer, ArrayList<Integer>> cache, CountThread countTread) {
        this.cache = cache;
        this.countTread = countTread;
    }

    @RequestMapping("/number")

    public ArrayList<Integer> showRandList(@RequestParam(value = "num") Integer num)
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
            cache.saveInCache(num, randVals);
        }
        LOGGER.info("Requests: " + Counter.getCountVal());
        return randVals;
    }
}
