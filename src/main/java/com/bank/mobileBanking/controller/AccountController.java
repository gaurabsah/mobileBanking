package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public Map<String, Object> getBalance(@RequestParam String accountNumber, @RequestParam String sPin) {
        return accountService.getBalance(accountNumber, sPin);
    }

    @GetMapping("/getAccount")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> getAccount(@RequestParam String accountNumber, @RequestParam String sPin) {
        return accountService.getAccountDetail(accountNumber, sPin);
    }

    @PostMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQrCode(@RequestParam String accountNumber, @RequestParam String securityPin) throws IOException {
        int width = 200; // Adjust the desired width of the QR code
        int height = 200; // Adjust the desired height of the QR code
        return accountService.generateQRCode(accountNumber, securityPin, width, height);
    }
}
