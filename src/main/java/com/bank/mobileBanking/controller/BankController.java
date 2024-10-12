package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/getBankDetailByBankName")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getBankDetailByBankName(@RequestParam String bankName){
        return bankService.getBankByBankName(bankName);
    }

    @GetMapping("/getBankDetailByIFSC")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getBankDetailByIFSC(@RequestParam String ifsc){
        return bankService.getBankByIFSC(ifsc);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String,Object> getAllBankDetails(){
        return bankService.getAllBanks();
    }

    @PostMapping("/saveBankDetails")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String,Object> saveBankDetail(@RequestBody BankDTO bankDTO){
        return bankService.saveBankDetail(bankDTO);
    }

    @GetMapping("/getBankByNameAndIFSC")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> getBankByNameAndIFSC(@RequestParam String bankName, @RequestParam String ifsc){
        return bankService.getBankByNameAndIFSC(bankName,ifsc);
    }
}
