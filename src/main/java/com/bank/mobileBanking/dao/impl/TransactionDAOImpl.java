package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dao.helper.TransactionDAOHelper;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.entity.Transaction;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.TransactionDateTimeNotFoundException;
import com.bank.mobileBanking.util.TransactionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionDAOImpl implements TransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;

    private final TransactionDAOHelper transactionDAOHelper;


    @Override
    public void transferMoney(TransactionDTO transactionDTO) {
        String transactionId = UUID.randomUUID().toString();
        LocalDateTime transactionTime = transactionDTO.getTime();
        if (transactionTime == null) {
            transactionTime = LocalDateTime.now();
        }
        try {
            jdbcTemplate.update(TransactionConstant.TRANSFER_MONEY,
                    transactionId,
                    transactionDTO.getAmount(),
                    transactionDTO.getSenderAccountNumber(),
                    transactionDTO.getReceiverAccountNumber(),
                    transactionTime
            );
        } catch (Exception e) {
            log.error("Error while sending money: {}", e.getMessage());
        }

    }

    @Override
    public TransactionDTO getTransaction(String txnId) {
        return jdbcTemplate.query(TransactionConstant.GET_TXN_DETAIL_BY_ID, new Object[]{txnId}, rs -> {
            TransactionDTO transactionDTO = new TransactionDTO();
            try {
                if (rs.next()) {
                    Transaction transaction = new Transaction();
                    transaction.setAmount(rs.getDouble("txn_amount"));
                    transaction.setTxnId(rs.getString("txn_id"));
                    transaction.setSenderAccountNumber(rs.getLong("sender_account_number"));
                    transaction.setReceiverAccountNumber(rs.getLong("receiver_account_number"));

                    LocalDateTime time = transactionDAOHelper.getTransactionTime(txnId);
                    if (time == null) {
                        throw new TransactionDateTimeNotFoundException("TXN Date not found");
                    }
                    transaction.setTime(time);

                    transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
                    return transactionDTO;
                } else {
                    return null;
                }
            } catch (Exception e) {
                log.error("Error in getting Bank Details :: {}", e.getMessage());
            }
            return transactionDTO;
        });
    }

    @Override
    public List<TransactionDTO> txnHistory(Long accountNumber) {
        return jdbcTemplate.query(TransactionConstant.GET_ALL_TXN_DETAILS, new Object[]{accountNumber}, rs -> {
            List<Transaction> transactionList = new ArrayList<>();

            try {
                while (rs.next()) {
                    Transaction txn = new Transaction();
                    txn.setReceiverAccountNumber(rs.getLong("receiver_account_number"));
                    txn.setSenderAccountNumber(rs.getLong("sender_account_number"));
                    txn.setAmount(rs.getDouble("txn_amount"));
                    txn.setTxnId(rs.getString("txn_id"));

                    LocalDateTime time = transactionDAOHelper.getTransactionTime(txn.getTxnId());
                    if (time == null) {
                        throw new TransactionDateTimeNotFoundException("TXN Date not found for txnId: " + txn.getTxnId());
                    }
                    txn.setTime(time);

                    transactionList.add(txn);
                }
            } catch (SQLException e) {
                log.error("SQL error in getting transaction history: {}", e.getMessage());
                throw new ResourcesNotFoundException("Error fetching transaction history");
            } catch (Exception e) {
                log.error("Error in getting transaction history: {}", e.getMessage());
                throw new RuntimeException("Error processing transaction history", e);
            }

            return transactionList.stream()
                    .map(s -> modelMapper.map(s, TransactionDTO.class))
                    .collect(Collectors.toList());
        });
    }
}
