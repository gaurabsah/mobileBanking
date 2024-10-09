package com.bank.mobileBanking.exception;

public class ResourcesAlreadyExistsException extends RuntimeException{

    public ResourcesAlreadyExistsException(String msg){
        super(msg);
    }
}
