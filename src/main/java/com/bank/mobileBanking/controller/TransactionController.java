package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/txn")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/sendMoney")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> sendMoney(@RequestBody TransactionDTO transactionDTO, @RequestParam String sPin){
        return transactionService.transferMoney(transactionDTO,sPin);
    }

    @GetMapping("/getTxn")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getTxn(@RequestParam String txnId, @RequestParam String sPin){
        return transactionService.getTransaction(txnId,sPin);
    }

    @GetMapping("/paymentHistory")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> paymentHistory(@RequestParam String accountNumber, @RequestParam String sPin){
        return transactionService.getTransactionHistory(accountNumber,sPin);
    }
}
