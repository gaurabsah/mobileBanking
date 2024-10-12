package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.TransactionDTO;

import java.util.Map;

public interface TransactionService {

    Map<String,Object> transferMoney(TransactionDTO transactionDTO,Long sPin);

    Map<String,Object> getTransaction(String txnId,Long sPin);

    Map<String,Object> getTransactionHistory(Long accountNumber, Long sPin);
}
