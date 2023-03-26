package com.example.lr1.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad arguments")
public class IllegalArguments extends Exception{
    public IllegalArguments(String message){
        super(message);
    }
}
