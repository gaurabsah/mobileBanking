package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.BankDAO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankDAO bankDAO;


    @Override
    public Map<String, Object> getBankByBankName(String bankName) {
        Map<String, Object> responseMap = new HashMap<>();
        BankDTO bankDetail = new BankDTO();

        if (null != bankName && !bankName.isEmpty()) {
            bankDetail = bankDAO.getBankByBankName(bankName);
        }

        responseMap.put("bankDetail", bankDetail);
        return responseMap;
    }

    @Override
    public Map<String, Object> getBankByIFSC(String ifsc) {
        Map<String, Object> responseMap = new HashMap<>();
        BankDTO bankDetail = new BankDTO();

        if (null != ifsc && !ifsc.isEmpty()) {
            bankDetail = bankDAO.getBankByIFSC(ifsc);
        }

        responseMap.put("bankDetail", bankDetail);
        return responseMap;
    }

    @Override
    public Map<String, Object> getAllBanks() {
        Map<String, Object> responseMap = new HashMap<>();
        List<BankDTO> bankDTOList = new ArrayList<>();
        bankDTOList = bankDAO.getAllBanks();
        responseMap.put("listOfBanks", bankDTOList);
        return responseMap;
    }

    @Override
    public Map<String, Object> saveBankDetail(BankDTO bankDTO) {
        Map<String, Object> responseMap = new HashMap<>();
        bankDAO.createBank(bankDTO);
        responseMap.put("success", "Bank Details saved successfully");
        return responseMap;
    }

    @Override
    public Map<String, Object> getBankByNameAndIFSC(String bankName, String ifsc) {
        Map<String, Object> responseMap = new HashMap<>();
        BankDTO bankDetail = new BankDTO();

        if (null != ifsc && !ifsc.isEmpty() && null != bankName && !bankName.isEmpty()) {
            bankDetail = bankDAO.getBankByNameAndIFSC(bankName, ifsc);
        }

        responseMap.put("bankDetail", bankDetail);
        return responseMap;
    }


}
