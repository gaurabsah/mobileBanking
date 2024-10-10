package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/createAccount")
    public Map<String,Object> createAccounts(@RequestBody AccountDTO accountDTO) {
       return accountService.createAccount(accountDTO);
    }
}
