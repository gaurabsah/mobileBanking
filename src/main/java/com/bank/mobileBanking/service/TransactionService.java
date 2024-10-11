package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.TransactionDTO;

import java.util.Map;

public interface TransactionService {

    Map<String,Object> transferMoney(TransactionDTO transactionDTO,Long sPin);
}
