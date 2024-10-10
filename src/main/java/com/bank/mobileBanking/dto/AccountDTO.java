package com.bank.mobileBanking.dto;

import com.bank.mobileBanking.entity.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDTO {

    private String accountHolderName;

    private Long accountNumber;

    private AccountType accountType;

    private Double balance;

    private Long pin;

    private BankDTO bankDTO;
}
