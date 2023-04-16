package com.example.lr1;

import org.springframework.web.bind.annotation.RestController;

import com.example.lr1.count.CountThread;
import com.example.lr1.count.Counter;
import com.example.lr1.exception.IllegalArguments;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Validated
@RestController
public class NumControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Cache<Integer, ArrayList<Integer>> cache;
    private CountThread countTread;
    public RandCalculate randCalculate;



    // @Autowired
    public NumControl(Cache<Integer, ArrayList<Integer>> cache, CountThread countTread) {
        this.cache = cache;
        this.countTread = countTread;
        // this.randCalculate = randCalculate;
    }

    public JSONObject listToAggJson(ArrayList<Integer> al) {
        int minInpVal = al.stream().min(Integer::compareTo).get();
        LOGGER.info("Min input value: " + minInpVal);

        int maxInpVal = al.stream().max(Integer::compareTo).get();
        LOGGER.info("Max input value: " + maxInpVal);

        OptionalDouble averInpVal = al.stream().mapToInt(n -> n).average();
        LOGGER.info("Average input value: " + averInpVal);
        
        JSONObject resp = new JSONObject();
        try{
        resp.put("minOutputValue: ", minInpVal);
        resp.put("maxOutputValue: ", maxInpVal);
        resp.put("averageOutputValue: ", averInpVal);
        resp.put("generated values: ", al.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return resp;
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

            Collections.sort(randVals, (o1, o2) -> (o1 < o2) ? -1 : (o1 > o2) ? 1 : 0);
            Collectors.toList();

            cache.saveInCache(num, randVals);

        }
        LOGGER.info("Requests: " + Counter.getCountVal());

        return randVals;
    }

    @RequestMapping("/number")
    public ArrayList<Integer> showRandList(@RequestParam(value = "num") Integer num)
            throws IllegalArgumentException, IllegalArguments {
        return generateNums(num);
    }

    @RequestMapping("/counter")
    public int getCounter() {
        return Counter.getCountVal();
    }

    @PostMapping(value = "/number_JSON", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> showRandListBulk(@RequestBody Map<String, Object> numsJSON) throws JSONException {
        ArrayList<Integer> nums;
        LOGGER.info("nums:" + numsJSON.toString());
        Stream<ArrayList<Integer>> result_lists;
        List<JSONObject> result_jsons = null;


        nums = (ArrayList<Integer>) (numsJSON.get("nums"));

        List<Integer> range = IntStream.rangeClosed(0, nums.size() - 1).boxed().toList();
        result_lists = range.stream().map(i -> {
            ArrayList<Integer> x = null;
            try {
                x = generateNums(nums.get(i));
            } catch (IllegalArgumentException | IllegalArguments e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                return x;
            }

        });//.collect(Collectors.toList());
            
        result_jsons = result_lists.map(i -> listToAggJson(i)).collect(Collectors.toList());
        

        int minInpVal = nums.stream().min(Integer::compareTo).get();
        LOGGER.info("Min input value: " + minInpVal);

        int maxInpVal = nums.stream().max(Integer::compareTo).get();
        LOGGER.info("Max input value: " + maxInpVal);

        OptionalDouble averInpVal = nums.stream().mapToInt(n -> n).average();
        LOGGER.info("Average input value: " + averInpVal);

        JSONObject resp = new JSONObject();
        resp.put("minInputValue: ", minInpVal);
        resp.put("maxInputValue: ", maxInpVal);
        resp.put("averageInputValue: ", averInpVal);
        resp.put("Input values: ", nums.toString());
        
        result_jsons.add(resp);

        /*
          int minOutVal, maxOutVal;
          OptionalDouble averOutVal;
          
          result.stream().map(i -> {
          minOutVal = i.stream().min(Integer::compareTo).get();
          LOGGER.info("Min output value: " + minOutVal);
          resp.put("minOutVal: ", minOutVal);
          
          maxOutVal = i.stream().max(Integer::compareTo).get();
          LOGGER.info("Max output value: " + maxOutVal);
          resp.put("maxOutVal: ", maxOutVal);
          
          averOutVal = i.stream().mapToInt(n -> n).average();
          LOGGER.info("Average output value: " + averOutVal);
          resp.put("averOutVal: ", averOutVal);
          });
        */
        
        return new ResponseEntity<>(result_jsons.toString(), HttpStatus.OK);
    }

}
