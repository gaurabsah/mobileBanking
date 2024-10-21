package com.bank.mobileBanking.dto;

import com.bank.mobileBanking.entity.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private String txnId;

    private Double amount;

    private String senderAccountNumber;

    private String receiverAccountNumber;

    private LocalDate time;

    private ServiceType service;

    private Double gst;

    private Double commission;

    public TransactionDTO(List<TransactionDTO> transactions) {
    }
}
