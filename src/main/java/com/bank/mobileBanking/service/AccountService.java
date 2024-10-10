package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.AccountDTO;

import java.util.Map;

public interface AccountService {

    Map<String,Object> createAccount(AccountDTO accountDTO);
}
