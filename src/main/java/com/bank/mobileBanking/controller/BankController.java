package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/getBankDetailByBankName")
    public Map<String,Object> getBankDetailByBankName(@RequestParam String bankName){
        return bankService.getBankByBankName(bankName);
    }

    @GetMapping("/getBankDetailByIFSC")
    public Map<String,Object> getBankDetailByIFSC(@RequestParam String ifsc){
        return bankService.getBankByIFSC(ifsc);
    }

    @GetMapping("/")
    public Map<String,Object> getAllBankDetails(){
        return bankService.getAllBanks();
    }

    @PostMapping("/saveBankDetails")
    public Map<String,Object> saveBankDetail(@RequestBody BankDTO bankDTO){
        return bankService.saveBankDetail(bankDTO);
    }
}
