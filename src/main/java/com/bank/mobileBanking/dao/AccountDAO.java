package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.AccountDTO;

public interface AccountDAO {

    void createAccount(AccountDTO accountDTO);

    double getBalance(Long accountNumber);

    AccountDTO getAccount(Long accountNumber);



}
