package com.bank.mobileBanking.dao.helper;

import com.bank.mobileBanking.dao.BankDAO;
import com.bank.mobileBanking.dto.BankDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountDAOHelper {

    private final JdbcTemplate jdbcTemplate;

    private final BankDAO bankDAO;

    public BankDTO getBankByNameAndIFSC(String bankName, String ifsc) {
        return bankDAO.getBankByNameAndIFSC(bankName, ifsc);
    }

    public LocalDate getUserDOB(int id) {
        String sql = "SELECT DOB FROM user_tbl WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, LocalDate.class);
    }
}
