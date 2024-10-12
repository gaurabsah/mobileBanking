package com.bank.mobileBanking.exception;

public class TransactionDateTimeNotFoundException extends RuntimeException {
    public TransactionDateTimeNotFoundException(String txnDateNotFound) {
        super(txnDateNotFound);
    }
}
