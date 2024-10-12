package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.BankDTO;

import java.util.Map;

public interface BankService {

    Map<String, Object> getBankByBankName(String bankName);

    Map<String, Object> getBankByIFSC(String ifsc);

    Map<String, Object> getAllBanks();

    Map<String, Object> saveBankDetail(BankDTO bankDTO);

    Map<String, Object> getBankByNameAndIFSC(String bankName, String ifsc);
}
