package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.TransactionDTO;

import java.util.List;

public interface TransactionDAO {

    void transferMoney(TransactionDTO transactionDTO);

    TransactionDTO getTransaction(String txnId);

    List<TransactionDTO> txnHistory(Long accountNumber);
}
