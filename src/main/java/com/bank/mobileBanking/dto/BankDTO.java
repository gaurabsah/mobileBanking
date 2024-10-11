package com.bank.mobileBanking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDTO {

    private String bankName;

    private String branch;

    private String ifsc;

    private List<AccountDTO> accountDTOList;
}
