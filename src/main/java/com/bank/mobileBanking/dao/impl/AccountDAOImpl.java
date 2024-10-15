package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.BankDAO;
import com.bank.mobileBanking.dao.UserDAO;
import com.bank.mobileBanking.dao.helper.AccountDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.BankDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.Account;
import com.bank.mobileBanking.entity.Bank;
import com.bank.mobileBanking.entity.User;
import com.bank.mobileBanking.entity.enums.AccountType;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.SomeThingWentWrongException;
import com.bank.mobileBanking.exception.TransactionDateTimeNotFoundException;
import com.bank.mobileBanking.util.AccountConstant;
import com.bank.mobileBanking.util.BankConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AccountDAOImpl implements AccountDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;

    private final AccountDAOHelper accountDAOHelper;

    private final BankDAO bankDAO;

    private final UserDAO userDAO;


    @Override
    public void createAccount(UserDTO userDTO, AccountDTO accountDTO) {
        log.info("inside createAccount():");

        if (userDTO == null || (accountDTO.getPin() == null) || (accountDTO.getAccountType() == null)) {
            throw new ResourcesNotFoundException("UserDTO and AccountDTO cannot be null.");
        }

        String accountNumber = UUID.randomUUID().toString();
        String address = userDTO.getAddress();

        User user = modelMapper.map(userDTO, User.class);
        // Fetch user ID and validate it
        int userId = user.getId();
        if (userId == 0) {
            log.error("User ID: {}", 0);
            throw new ResourcesNotFoundException("Valid User ID is required.");
        }

        log.info("checking for bank info");
        Bank bank = bankDAO.getBankByBranch(address);
        log.info("bank info: {}", bank);
        if (bank == null || bank.getId() == 0) {
            throw new ResourcesNotFoundException("Valid Bank Detail Not Found for branch: " + userDTO.getAddress());
        }

        try {
            log.info("insert");
            jdbcTemplate.update(AccountConstant.OPEN_ACCOUNT,
                    accountNumber,
                    accountDTO.getAccountType().name(),
                    0.0,
                    accountDTO.getPin(),
                    bank.getId(),
                    userId);
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage());
            throw new RuntimeException("Unable to create account", e);
        }
    }

    @Override
    public AccountDTO getBalance(String accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_BALANCE, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    Account account = new Account();
                    account.setAccountNumber(rs.getString("account_number"));

                    // Convert string to AccountType enum
                    String accountTypeString = rs.getString("account_type");
                    AccountType accountType = AccountType.valueOf(accountTypeString);
                    accountDTO.setAccountType(accountType);

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
    public AccountDTO getAccount(String accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_DETAIL, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    accountDTO.setAccountNumber(rs.getString("account_number"));

                    // Convert string to AccountType enum
                    String accountTypeString = rs.getString("account_type");
                    AccountType accountType = AccountType.valueOf(accountTypeString);
                    accountDTO.setAccountType(accountType);

                    accountDTO.setBalance(rs.getDouble("bank_balance"));
                    accountDTO.setPin(rs.getString("security_pin"));

                    BankDTO bankDTO = new BankDTO();
                    bankDTO.setBankName(rs.getString("bank_name"));
                    bankDTO.setBranch(rs.getString("branch"));
                    bankDTO.setIfsc(rs.getString("ifsc_code"));
                    accountDTO.setBankDTO(bankDTO);

                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));

                    LocalDate dob = accountDAOHelper.getUserDOB(user.getId());
                    if (dob == null) {
                        throw new TransactionDateTimeNotFoundException("User DateOfBirth not found for Id: " + user.getId());
                    }
                    user.setDob(dob);
                    user.setMobileNumber(rs.getString("mobile_number"));
                    user.setPan(rs.getString("pan_card_number"));
                    user.setAddress(rs.getString("address"));
                    user.setPinCode(rs.getString("pin_code"));
                    user.setState(rs.getString("state"));
                    user.setCountry(rs.getString("country"));
                    user.setIsLogged(rs.getBoolean("is_logged"));
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    accountDTO.setUserDTO(userDTO);

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
    public String getSecurityPin(String accountNumber) {
        return jdbcTemplate.query(AccountConstant.GET_ACCOUNT_SECURITY_PIN, new Object[]{accountNumber}, rs -> {
            AccountDTO accountDTO = new AccountDTO();
            try {
                if (rs.next()) {
                    Account account = new Account();
                    account.setPin(rs.getString("security_pin"));
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

    @Override
    public boolean openAccount(String email, AccountDTO accountDTO) {
        log.info("inside createAccount():");

        if (email == null || email.isEmpty()) {
            throw new ResourcesNotFoundException("email cannot be null.");
        }

        UserDTO userDTO = userDAO.getUser(email);

        if (userDTO == null || (accountDTO.getPin() == null) || (accountDTO.getAccountType() == null)) {
            throw new ResourcesNotFoundException("UserDTO and AccountDTO cannot be null.");
        }

        if (!userDTO.getIsLogged()){
            log.info("Please login...");
            throw new SomeThingWentWrongException("Please login...");
        }

        String accountNumber = UUID.randomUUID().toString();
        String address = userDTO.getAddress();

        log.info("user {}",userDTO);
        // Fetch user ID and validate it
        int userId = userDTO.getId();
        if (userId == 0) {
            log.error("User ID: {}", 0);
            throw new ResourcesNotFoundException("Valid User ID is required.");
        }

        log.info("checking for bank info");
        Bank bank = bankDAO.getBankByBranch(address);
        log.info("bank info: {}", bank);
        if (bank == null || bank.getId() == 0) {
            throw new ResourcesNotFoundException("Valid Bank Detail Not Found for branch: " + userDTO.getAddress());
        }

        try {
            log.info("insert");
            int rowsAffected = jdbcTemplate.update(AccountConstant.OPEN_ACCOUNT,
                    accountNumber,
                    accountDTO.getAccountType().name(),
                    0.0,
                    accountDTO.getPin(),
                    bank.getId(),
                    userId);
            return rowsAffected > 0;
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage());
            throw new RuntimeException("Unable to create account", e);
        }
    }

}
