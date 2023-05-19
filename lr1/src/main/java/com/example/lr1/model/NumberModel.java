package com.example.lr1.model;

import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "number")
public class NumberModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "number")
    private Integer number;
    @Column(name = "genRandVals")
    private ArrayList<Integer> generatedVals;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNum(Integer number) {
        this.number = number;
    }

    public Integer getNum() {
        return number;
    }

    public void setGneneratedVals(ArrayList<Integer> generatedVals) {
        this.generatedVals = generatedVals;
    }

    public ArrayList<Integer> getGeneratedVals() {
        return generatedVals;
    }

    public String toString() {
        return "number: " + number + " generated values: " + generatedVals;
    }

}
