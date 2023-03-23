package com.example.lr1;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
public class NumControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Cache<Integer, Map<Integer, Integer>> cache;

    //@Autowired
    public NumControl(Cache<Integer, Map<Integer, Integer>> cache) {
        this.cache = cache;
    }

    @RequestMapping("/number")

    public Map<Integer, Integer> showRandList(@RequestParam(value = "num") @Min(5) Integer num)
            throws IllegalArgumentException {

        LOGGER.info("Incoming number: " + num);
        if (num < 5) {
            LOGGER.error("Entered number shall not be less than 5. Your number: " + num);
        }
        if (num > 100) {
            LOGGER.error("Entered number must not be greater than 100");
            throw new IllegalArgumentException("Entered number must not be greater than 100");
        }
        Map<Integer, Integer> map = new HashMap<>();

        if (cache.contain(num)) {
            LOGGER.info("Using cache");
            map = cache.getFromCache(num);
        } else {
            for (int i = 1; i <= 5; i++) {
                int yourNumber = (int) (Math.random() * (num + 1));
                LOGGER.info("Generated value: " + yourNumber);
                map.put(i, yourNumber);
            }
            cache.saveInCache(num, map);
        }

        return map;
    }
}
