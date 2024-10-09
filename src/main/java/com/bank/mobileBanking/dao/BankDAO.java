package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.BankDTO;

import java.util.List;

public interface BankDAO {

    void createBank(BankDTO bankDTO);

    List<BankDTO> getAllBanks();

    BankDTO getBankByBankName(String bankName);

    BankDTO getBankByIFSC(String ifsc);
}