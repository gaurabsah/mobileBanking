package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/checkBalance")
    public Map<String,Object> getBalance(@RequestParam Long accountNumber, @RequestParam Long sPin) {
        return accountService.getBalance(accountNumber,sPin);
    }

    @GetMapping("/getAccount")
    public Map<String,Object> getAccount(@RequestParam Long accountNumber, @RequestParam Long sPin) {
        return accountService.getAccountDetail(accountNumber,sPin);
    }
}
