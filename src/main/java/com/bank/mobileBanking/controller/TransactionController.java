package com.bank.mobileBanking.controller;

import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.service.TransactionService;
import com.bank.mobileBanking.util.PDF;
import com.bank.mobileBanking.util.XML;
import com.google.zxing.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/txn")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionDAO transactionDAO;

    private final PDF pdf;

    private final XML xml;

    @PostMapping("/sendMoney")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> sendMoney(@RequestBody TransactionDTO transactionDTO, @RequestParam String sPin) {
        return transactionService.transferMoney(transactionDTO, sPin);
    }

    @GetMapping("/getTxn")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> getTxn(@RequestParam String txnId, @RequestParam String sPin) {
        return transactionService.getTransaction(txnId, sPin);
    }

    @GetMapping("/paymentHistory")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> paymentHistory(@RequestParam String accountNumber, @RequestParam String sPin) {
        return transactionService.getTransactionHistory(accountNumber, sPin);
    }

    @PostMapping(value = "/qrCodePayment", consumes = "multipart/form-data")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "returns decoded information inside provided QR code")
    public Map<String, Object> readQrCodeAndPay(
            @Parameter(description = ".png image of QR code generated through this portal")
            @RequestParam String accountNumber,
            @RequestParam Double amount,
            @RequestParam String securityPin,
            @RequestParam(value = "file", required = true) MultipartFile file)
            throws IOException, NotFoundException {
        return transactionService.readQrCodeAndPay(accountNumber, amount, securityPin, file);
    }

    @GetMapping("/generateStatement")
    @ResponseStatus(HttpStatus.FOUND)
    public Map<String, Object> generateStatement(@RequestParam String accountNumber,
                                                 @RequestParam String startDate,
                                                 @RequestParam String endDate,
                                                 @RequestParam String sPin) {
        return transactionService.generateStatement(accountNumber, startDate, endDate, sPin);
    }

    public ResponseEntity<byte[]> downloadStatement(String accountNumber, String startDate, String endDate, String fileType) {
        List<TransactionDTO> transactions = transactionDAO.getAllTransactions(accountNumber, startDate, endDate);

        byte[] fileData;
        String contentType;

        try {
            if ("pdf".equalsIgnoreCase(fileType)) {
                fileData = pdf.createPdf(transactions);
                contentType = "application/pdf";
            } else if ("xml".equalsIgnoreCase(fileType)) {
                fileData = xml.createXml(transactions);
                contentType = "application/xml";
            } else {
                return ResponseEntity.badRequest().body("Invalid file type specified".getBytes());
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage().getBytes());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"statement." + fileType + "\"")
                .body(fileData);
    }

}
