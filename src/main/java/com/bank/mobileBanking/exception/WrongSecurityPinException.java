package com.bank.mobileBanking.exception;

public class WrongSecurityPinException extends RuntimeException {
    public WrongSecurityPinException(String s) {
        super(s);
    }
}
