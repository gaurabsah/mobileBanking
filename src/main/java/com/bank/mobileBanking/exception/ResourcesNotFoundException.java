package com.bank.mobileBanking.exception;

public class ResourcesNotFoundException extends RuntimeException{

    public ResourcesNotFoundException(String msg){
        super(msg);
    }
}
