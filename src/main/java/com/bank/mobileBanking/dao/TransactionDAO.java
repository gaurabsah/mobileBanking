package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.TransactionDTO;

public interface TransactionDAO {

    void transferMoney(TransactionDTO transactionDTO);
}
