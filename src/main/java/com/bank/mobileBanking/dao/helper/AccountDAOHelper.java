package com.bank.mobileBanking.dao.helper;

import com.bank.mobileBanking.dao.BankDAO;
import com.bank.mobileBanking.dto.BankDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountDAOHelper {

    private final BankDAO bankDAO;

    public BankDTO getBankByNameAndIFSC(String bankName,String ifsc) {
        return bankDAO.getBankByNameAndIFSC(bankName, ifsc);
    }
}
