package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.entity.Bank;

import java.util.List;

public interface BankDAO {

    void createBank(BankDTO bankDTO);

    List<BankDTO> getAllBanks();

    BankDTO getBankByBankName(String bankName);

    BankDTO getBankByIFSC(String ifsc);

    BankDTO getBankByNameAndIFSC(String bankName,String ifsc);

    Bank getBankByBranch(String branch);

    BankDTO getBankByNameAndBranch(String bankName, String branch);
}
