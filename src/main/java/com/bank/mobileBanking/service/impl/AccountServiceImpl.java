package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.exception.WrongSecurityPinException;
import com.bank.mobileBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

//    @Override
//    public Map<String, Object> createAccount(AccountDTO accountDTO) {
//        Map<String, Object> responseMap = new HashMap<>();
//        accountDAO.createAccount(accountDTO);
//        responseMap.put("success", "Account created successfully");
//        return responseMap;
//    }

    @Override
    public Map<String, Object> getBalance(String accountNumber, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        Long securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin" + securityPin);
        }
        AccountDTO balance = accountDAO.getBalance(accountNumber);
        responseMap.put("Bank account balance fetched successfully", balance);
        return responseMap;
    }

    @Override
    public Map<String, Object> getAccountDetail(String accountNumber, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        Long securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin" + securityPin);
        }
        AccountDTO account = accountDAO.getAccount(accountNumber);
        responseMap.put("account detail", account);
        return responseMap;
    }
}
