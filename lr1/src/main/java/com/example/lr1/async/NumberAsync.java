package com.example.lr1.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.lr1.exception.IllegalArguments;
import com.example.lr1.logicofcomputing.RandCalculate;
import com.example.lr1.model.NumberModel;
import com.example.lr1.service.NumberServ;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class NumberAsync {

    private static final Logger LOGGER = LogManager.getLogger();
    private NumberServ numberServ;
    public RandCalculate randCalculate = new RandCalculate();

    @Autowired
    public NumberAsync(NumberServ numberServ) {

        this.numberServ = numberServ;
    }

    public Integer createNewAsync(NumberModel numberModel) {

        NumberModel numModelAsync = new NumberModel();
        numModelAsync.setNum(numberModel.getNum());
        numberServ.save(numModelAsync);
        return numModelAsync.getId();
    }

    public CompletableFuture<Integer> calculateAsync(int id) {
        return CompletableFuture.supplyAsync(() -> {
                NumberModel asyncCalcResult = numberServ.findNecessary(id);
                try {
                    asyncCalcResult.setGneneratedVals(randCalculate.generateNums(asyncCalcResult.getNum()));
                } catch (IllegalArgumentException | IllegalArguments | InterruptedException e) {
                   
                    LOGGER.info("Wrong argument!");
                    
                }
                numberServ.save(asyncCalcResult);
                return asyncCalcResult.getId();
        });
    }
}
