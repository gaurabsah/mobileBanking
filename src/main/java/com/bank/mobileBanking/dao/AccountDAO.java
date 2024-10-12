package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.UserDTO;

public interface AccountDAO {

    void createAccount(UserDTO userDTO, AccountDTO accountDTO);

    AccountDTO getBalance(String accountNumber);

    AccountDTO getAccount(String accountNumber);

    Long getSecurityPin(String accountNumber);

    void updateAccountBalance(AccountDTO senderAccount);
}
