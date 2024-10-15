package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.AccountDTO;

import java.util.Map;

public interface AccountService {

//    Map<String, Object> createAccount(AccountDTO accountDTO);

    Map<String, Object> getBalance(String accountNumber, String sPin);

    Map<String, Object> getAccountDetail(String accountNumber, String sPin);
}
