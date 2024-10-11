package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.AccountDTO;

public interface AccountDAO {

    void createAccount(AccountDTO accountDTO);

    AccountDTO getBalance(Long accountNumber);

    AccountDTO getAccount(Long accountNumber);

    Long getSecurityPin(Long accountNumber);

    void updateAccountBalance(AccountDTO senderAccount);
}
