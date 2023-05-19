package com.example.lr1;

import org.springframework.web.bind.annotation.RestController;

import com.example.lr1.async.NumberAsync;
import com.example.lr1.cache.Cache;
import com.example.lr1.count.CountThread;
import com.example.lr1.count.Counter;
import com.example.lr1.exception.IllegalArguments;
import com.example.lr1.logicOfComputing.FindMinMaxAver;
import com.example.lr1.logicOfComputing.RandCalculate;
import com.example.lr1.model.NumberModel;
import com.example.lr1.service.NumberServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Validated
@RestController
public class NumControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Cache<Integer, ArrayList<Integer>> cache;
    private CountThread countTread;
    public RandCalculate randCalculate = new RandCalculate();
    public FindMinMaxAver filters = new FindMinMaxAver();
    private NumberServ numberServ;

    @Autowired
    private NumberAsync numberAsync = new NumberAsync(numberServ);

    @Autowired
    public NumControl(Cache<Integer, ArrayList<Integer>> cache, CountThread countTread, NumberServ numberServ) {
        this.cache = cache;
        this.countTread = countTread;
        this.numberServ = numberServ;
    }

    @RequestMapping("/number")
    public ArrayList<Integer> showRandList(@RequestParam(value = "num") Integer num)
            throws IllegalArgumentException, IllegalArguments {

        countTread.start();
        NumberModel numberModel = new NumberModel();
        ArrayList<Integer> yourRandVals = new ArrayList<Integer>();
        LOGGER.info("Incoming number: " + num);
        LOGGER.info("Requests: " + Counter.getCountVal());
        numberModel.setNum(num);
        if (cache.contain(num)) {
            LOGGER.info("Using cache");
            return cache.getFromCache(num);
        } else {
            yourRandVals = randCalculate.generateNums(num);
            LOGGER.info("Generated value: " + yourRandVals);
            cache.saveInCache(num, yourRandVals);

        }
        numberModel.setGneneratedVals(yourRandVals);
        numberServ.save(numberModel);
        return yourRandVals;
    }

    @RequestMapping("/counter")
    public int getCounter() {
        return Counter.getCountVal();
    }

    @PostMapping(value = "/number_JSON", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> showRandListBulk(@RequestBody Map<String, Object> numsJSON) throws JSONException {

        countTread.start();
        ArrayList<Integer> nums;
        LOGGER.info("nums:" + numsJSON.toString());
        Stream<ArrayList<Integer>> result_lists;
        List<JSONObject> result_jsons = null;

        nums = (ArrayList<Integer>) (numsJSON.get("nums"));

        List<Integer> range = IntStream.rangeClosed(0, nums.size() - 1).boxed().toList();
        result_lists = range.stream().map(i -> {
            ArrayList<Integer> x = null;
            try {
                if (cache.contain(nums.get(i))) {
                    LOGGER.info("Using cache");
                    x = cache.getFromCache(nums.get(i));
                } else {

                    x = randCalculate.generateNums(nums.get(i));
                    cache.saveInCache(nums.get(i), x);
                }

            } catch (IllegalArgumentException | IllegalArguments e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                return x;
            }

        });

        LOGGER.info("Output results ");
        result_jsons = result_lists.map(i -> filters.listToAggJson(i)).collect(Collectors.toList());

        JSONObject resp = new JSONObject();
        LOGGER.info("Input results");
        resp = filters.listToAggJson(nums);

        result_jsons.add(resp);

        LOGGER.info("Requests: " + Counter.getCountVal());

        return new ResponseEntity<>(result_jsons.toString(), HttpStatus.OK);
    }

    @GetMapping("/result/{id}")
    public NumberModel result(@PathVariable("id") int id) {
        return numberServ.findNecessary(id);
    }

    @PostMapping("/async")
    public Integer asyncCall(@RequestBody NumberModel numberModel) {

        int id = numberAsync.createNewAsync(numberModel);
        numberAsync.calculateAsync(id);
        return id;
    }

}
