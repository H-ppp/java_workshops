package com.example.lr1.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.lr1.exception.IllegalArguments;
import com.example.lr1.logicOfComputing.RandCalculate;
import com.example.lr1.model.NumberModel;
import com.example.lr1.service.NumberServ;

@Component
public class NumberAsync {

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
            try {
                NumberModel asyncCalcResult = numberServ.findNecessary(id);
                Thread.sleep(15000);
                try {
                    asyncCalcResult.setGneneratedVals(randCalculate.generateNums(asyncCalcResult.getNum()));
                } catch (IllegalArgumentException | IllegalArguments e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                numberServ.save(asyncCalcResult);
                return asyncCalcResult.getId();
            } catch (InterruptedException x) {
                throw new RuntimeException();
            }
        });
    }
}
