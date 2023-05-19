package com.example.lr1.logicOfComputing;


import java.util.ArrayList;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class FindMinMaxAver {
    
    public FindMinMaxAver()
    {
    }
    private static final Logger LOGGER = LogManager.getLogger();

    public JSONObject listToAggJson(ArrayList<Integer> al) {

        
        LOGGER.info("Generated values: " + al.toString());
        int minVal = al.stream().min(Integer::compareTo).get();
        LOGGER.info("Min value: " + minVal);

        int maxVal = al.stream().max(Integer::compareTo).get();
        LOGGER.info("Max value: " + maxVal);

        Double averVal = al.stream().mapToInt(n -> n).average().orElseThrow( IllegalArgumentException :: new);
        LOGGER.info("Average value: " + averVal);

        
        JSONObject resp = new JSONObject();
        try {
            resp.put("min value: ", minVal);
            resp.put("max value: ", maxVal);
            resp.put("average value: ", averVal);
            resp.put("generated values: ", al.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resp;
    }
}
