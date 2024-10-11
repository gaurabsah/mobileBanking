package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.helper.AccountDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.entity.Account;
import com.bank.mobileBanking.entity.enums.AccountType;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.util.AccountConstant;
import com.bank.mobileBanking.util.BankConstant;
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

    private final AccountDAOHelper accountDAOHelper;


    @Override
    public void createAccount(AccountDTO accountDTO) {
        log.info("inside createAccount():");
        // Check if bank information is valid
        if (accountDTO.getBankDTO() == null) {
            throw new IllegalArgumentException("Bank information is required.");
        }

        String bankName = accountDTO.getBankDTO().getBankName();
        log.info("Bank Name: {}",bankName);

        String bankIFSC = accountDTO.getBankDTO().getIfsc();
        log.info("Bank IFSC Code: {}",bankIFSC);

        // Check if the bank exists
        BankDTO bankDTO = accountDAOHelper.getBankByNameAndIFSC(bankName,bankIFSC);
        if (bankDTO==null) {
            throw new ResourcesNotFoundException("Bank not found: " + bankName);
        }

        Integer bankId = jdbcTemplate.queryForObject(BankConstant.GET_BANK_ID, new Object[]{
                bankName,bankIFSC
        }, Integer.class);

        try {
            jdbcTemplate.update(AccountConstant.INSERT_ACCOUNT_DETAIL, accountDTO.getAccountHolderName(),
                    accountDTO.getAccountNumber(),
                    accountDTO.getAccountType().name(),
                    accountDTO.getBalance(),
                    accountDTO.getPin(),
                    bankId);
            log.info("account detail: {}",accountDTO.toString());
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage());
            throw new RuntimeException("Unable to create account", e);
        }
    }

    @Override
    public AccountDTO getBalance(Long accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_BALANCE, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountHolderName(rs.getString("account_holder_name"));
                    account.setAccountNumber(rs.getLong("account_number"));

                    // Convert string to AccountType enum
                    String accountTypeString = rs.getString("account_type");
                    AccountType accountType = AccountType.valueOf(accountTypeString);
                    accountDTO.setAccountType(accountType); // Assuming you have a setAccountType method in AccountDTO


                    account.setBalance(rs.getDouble("bank_balance"));
                    accountDTO = modelMapper.map(account, AccountDTO.class);
                    return accountDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Account Balance :: {}", e.getMessage());
            }
            return accountDTO;
        });
    }

    @Override
    public AccountDTO getAccount(Long accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_DETAIL, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    accountDTO.setAccountHolderName(rs.getString("account_holder_name"));
                    accountDTO.setAccountNumber(rs.getLong("account_number"));

                    // Convert string to AccountType enum
                    String accountTypeString = rs.getString("account_type");
                    AccountType accountType = AccountType.valueOf(accountTypeString);
                    accountDTO.setAccountType(accountType); // Assuming you have a setAccountType method in AccountDTO


                    accountDTO.setBalance(rs.getDouble("bank_balance"));
                    accountDTO.setPin(rs.getLong("security_pin"));

                    BankDTO bankDTO = new BankDTO();
                    bankDTO.setBankName(rs.getString("bank_name"));
                    bankDTO.setBranch(rs.getString("branch"));
                    bankDTO.setIfsc(rs.getString("ifsc_code"));

                    accountDTO.setBankDTO(bankDTO);
                    return accountDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Account Balance :: {}", e.getMessage());
            }
            return accountDTO;
        });
    }

    @Override
    public Long getSecurityPin(Long accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_SECURITY_PIN, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    Account account = new Account();
                    account.setPin(rs.getLong("security_pin"));
                    accountDTO = modelMapper.map(account, AccountDTO.class);
                    return accountDTO.getPin();
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Account Balance :: {}", e.getMessage());
            }
            return accountDTO.getPin();
        });
    }

    @Override
    public void updateAccountBalance(AccountDTO senderAccount) {
        jdbcTemplate.update(AccountConstant.UPDATE_ACCOUNT_BALANCE, senderAccount.getBalance(), senderAccount.getAccountNumber());
    }

}
