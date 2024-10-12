package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

//    @PostMapping("/createAccount")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Map<String,Object> createAccounts(@RequestBody AccountDTO accountDTO) {
//       return accountService.createAccount(accountDTO);
//    }

    @GetMapping("/checkBalance")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getBalance(@RequestParam String accountNumber, @RequestParam Long sPin) {
        return accountService.getBalance(accountNumber,sPin);
    }

    @GetMapping("/getAccount")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getAccount(@RequestParam String accountNumber, @RequestParam Long sPin) {
        return accountService.getAccountDetail(accountNumber,sPin);
    }
}
