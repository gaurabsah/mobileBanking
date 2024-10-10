package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.BankDAO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.entity.Bank;
import com.bank.mobileBanking.util.BankConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BankDAOImpl implements BankDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;


    @Override
    public void createBank(BankDTO bankDTO) {
        jdbcTemplate.update(BankConstant.INSERT_BANK_DETAIL, new Object[]{
                bankDTO.getBankName(), bankDTO.getBranch(), bankDTO.getIfsc()
        });
    }

    @Override
    public List<BankDTO> getAllBanks() {
        return jdbcTemplate.query(BankConstant.GET_ALL_BANK_DETAIL, new Object[]{}, rs -> {
            List<Bank> bankList = new ArrayList<>();
            try {
                while (rs.next()) {
                    Bank bank = new Bank();
                    bank.setBankName(rs.getString("bank_name"));
                    bank.setBranch(rs.getString("branch"));
                    bank.setIfsc(rs.getString("ifsc_code"));
                    bankList.add(bank);
                }
            } catch (Exception e) {
                log.error("Error in getting Bank Details :: {}", e.getMessage());
            }
            return bankList.stream()
                    .map(s -> modelMapper.map(s, BankDTO.class))
                    .collect(Collectors.toList());
        });
    }

    @Override
    public BankDTO getBankByBankName(String bankName) {
        return jdbcTemplate.query(BankConstant.GET_BANK_DETAIL_BY_BANK_NAME, new Object[]{bankName}, rs -> {
            BankDTO bankDTO = new BankDTO();
            try {
                if (rs.next()) {
                    Bank bank = new Bank();
                    bank.setBankName(rs.getString("bank_name"));
                    bank.setBranch(rs.getString("branch"));
                    bank.setIfsc(rs.getString("ifsc_code"));
                    bankDTO = modelMapper.map(bank, BankDTO.class);
                    return bankDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Bank Details :: {}", e.getMessage());
            }
            return bankDTO;
        });
    }

    @Override
    public BankDTO getBankByIFSC(String ifsc) {
        return jdbcTemplate.query(BankConstant.GET_BANK_DETAIL_BY_IFSC, new Object[]{ifsc}, rs -> {
            BankDTO bankDTO = new BankDTO();
            try {
                if (rs.next()) {
                    Bank bank = new Bank();
                    bank.setBankName(rs.getString("bank_name"));
                    bank.setBranch(rs.getString("branch"));
                    bank.setIfsc(rs.getString("ifsc_code"));
                    bankDTO = modelMapper.map(bank, BankDTO.class);
                    return bankDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Bank Details :: {}", e.getMessage());
            }
            return bankDTO;
        });
    }

    @Override
    public BankDTO getBankByNameAndIFSC(String bankName, String ifsc) {
        return jdbcTemplate.query(BankConstant.GET_BANK_DETAIL_BY_NAME_AND_IFSC, new Object[]{bankName,ifsc}, rs -> {
            BankDTO bankDTO = new BankDTO();
            try {
                if (rs.next()) {
                    Bank bank = new Bank();
                    bank.setBankName(rs.getString("bank_name"));
                    bank.setBranch(rs.getString("branch"));
                    bank.setIfsc(rs.getString("ifsc_code"));
                    bankDTO = modelMapper.map(bank, BankDTO.class);
                    return bankDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Bank Details :: {}", e.getMessage());
            }
            return bankDTO;
        });
    }
}
