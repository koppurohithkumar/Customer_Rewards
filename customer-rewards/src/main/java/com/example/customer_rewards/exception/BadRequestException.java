package com.example.customer_rewards.exception;

public class BadRequestException extends  RuntimeException{
    public  BadRequestException(String msg){
        super(msg);
    }
}
