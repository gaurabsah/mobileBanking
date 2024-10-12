package com.bank.mobileBanking.dto;

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

    private Long senderAccountNumber;

    private Long receiverAccountNumber;

    private LocalDateTime time;
}
