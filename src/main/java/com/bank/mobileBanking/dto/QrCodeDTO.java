package com.bank.mobileBanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QrCodeDTO {

    private String name;
    private String accountNumber;
    private String accountType;
}
