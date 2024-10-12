package com.bank.mobileBanking.dao.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TransactionDAOHelper {

    private final JdbcTemplate jdbcTemplate;

    public LocalDateTime getTransactionTime(String txnId) {
        String sql = "SELECT txn_date_time FROM txn_tbl WHERE txn_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{txnId}, LocalDateTime.class);
    }

    public String getAccountNumberByTxnId(String txnId) {
        String sql = "SELECT sender_account_number FROM txn_tbl WHERE txn_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{txnId}, String.class);
    }
}
