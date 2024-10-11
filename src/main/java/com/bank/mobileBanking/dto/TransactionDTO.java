package com.bank.mobileBanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private String txnId;

    private Double amount;

    private Long senderAccountNumber;

    private Long receiverAccountNumber;
}
