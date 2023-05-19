package com.example.lr1.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lr1.model.NumberModel;
import com.example.lr1.repository.NumberRepo;

@Service
public class NumberServ {

    private NumberRepo numberRepo;

    @Autowired
    public NumberServ(NumberRepo numberRepo) {
        this.numberRepo = numberRepo;
    }

    public void save(NumberModel numberModel) {
        numberRepo.save(numberModel);
    }

    public NumberModel findNecessary(int id) {

        Optional<NumberModel> numberModel = numberRepo.findById(id);
        return numberModel.orElse(null);

    }
}
