package com.bank.mobileBanking.dto;

import com.bank.mobileBanking.entity.enums.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private String txnId;

    private Double amount;

    private String senderAccountNumber;

    private String receiverAccountNumber;

    private LocalDateTime time;

    private ServiceType service;

    private Double gst;

    private Double commission;
}
