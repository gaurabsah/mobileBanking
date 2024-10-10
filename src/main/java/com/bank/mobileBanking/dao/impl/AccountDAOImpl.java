package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.entity.Bank;
import com.bank.mobileBanking.util.AccountConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountDAOImpl implements AccountDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;


    @Override
    public void createAccount(AccountDTO accountDTO) {
        // Create and save the Bank entity
        Bank bank = new Bank();
        bank.setBankName(accountDTO.getBankDTO().getBankName());
        jdbcTemplate.update("INSERT INTO bank_table (name) VALUES (?)", bank.getBankName());

        // Retrieve the generated bank ID
        String bankname = jdbcTemplate.query("SELECT LAST_INSERT_ID()", new Object[]{accountDTO.getBankDTO().getBankName()});

        // Create and save the Account entity
        jdbcTemplate.update(AccountConstant.INSERT_ACCOUNT_DETAIL, new Object[]{
                accountDTO.getAccountHolderName(),
                accountDTO.getAccountNumber(),
                accountDTO.getBalance(),
                accountDTO.getPin(),
                bankId // Use the generated bank ID
        });
    }

    @Override
    public double getBalance(Long accountNumber) {
        return 0;
    }

    @Override
    public AccountDTO getAccount(Long accountNumber) {
        return null;
    }
}
