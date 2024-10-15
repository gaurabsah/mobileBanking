package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.AccountDTO;

import java.io.IOException;
import java.util.Map;

public interface AccountService {

//    Map<String, Object> createAccount(AccountDTO accountDTO);

    Map<String, Object> getBalance(String accountNumber, String sPin);

    Map<String, Object> getAccountDetail(String accountNumber, String sPin);

    byte[] generateQRCode(String accountNumber, String securityPin, int width, int height) throws IOException;

//    TODO: DEBIT CARD & CREDIT CARD
}
