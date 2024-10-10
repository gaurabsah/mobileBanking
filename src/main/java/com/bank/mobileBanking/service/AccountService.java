package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.AccountDTO;

import java.util.Map;

public interface AccountService {

    Map<String,Object> createAccount(AccountDTO accountDTO);

    Map<String,Object> getBalance(Long accountNumber, Long sPin);

    Map<String,Object> getAccountDetail(Long accountNumber, Long sPin);
}
