package com.example.lr1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lr1.model.NumberModel;

@Repository
public interface NumberRepo extends JpaRepository<NumberModel, Integer> {
}
