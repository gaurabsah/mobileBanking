package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.TransactionDTO;

import java.util.Map;

public interface TransactionService {

    Map<String, Object> transferMoney(TransactionDTO transactionDTO, String sPin);

    Map<String, Object> getTransaction(String txnId, String sPin);

    Map<String, Object> getTransactionHistory(String accountNumber, String sPin);

//    TODO: QR-CODE Scanning and pay

    //    TODO: Generating Bank Account Statement and sending via Email or Download
    Map<String, Object> generateStatement(String accountNumber, String startDate, String endDate, String sPin);
}
