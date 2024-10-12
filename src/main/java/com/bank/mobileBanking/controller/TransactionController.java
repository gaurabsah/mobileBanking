package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/txn")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/sendMoney")
    public Map<String,Object> sendMoney(@RequestBody TransactionDTO transactionDTO, @RequestParam Long sPin){
        return transactionService.transferMoney(transactionDTO,sPin);
    }

    @GetMapping("/getTxn")
    public Map<String,Object> getTxn(@RequestParam String txnId, @RequestParam Long sPin){
        return transactionService.getTransaction(txnId,sPin);
    }

    @GetMapping("/paymentHistory")
    public Map<String,Object> paymentHistory(@RequestParam Long accountNumber, @RequestParam Long sPin){
        return transactionService.getTransactionHistory(accountNumber,sPin);
    }
}