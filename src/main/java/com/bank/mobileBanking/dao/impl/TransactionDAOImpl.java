package com.bank.mobileBanking.dao.impl;

import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.util.TransactionConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TransactionDAOImpl implements TransactionDAO {

    private final JdbcTemplate jdbcTemplate;

    private final ModelMapper modelMapper;


    @Override
    public void transferMoney(TransactionDTO transactionDTO) {
        String transactionId = UUID.randomUUID().toString();
        try {
            jdbcTemplate.update(TransactionConstant.TRANSFER_MONEY,
                    transactionId,
                    transactionDTO.getAmount(),
                    transactionDTO.getSenderAccountNumber(),
                    transactionDTO.getReceiverAccountNumber()
            );
        } catch (Exception e){
            log.error("Error while sending money: {}",e.getMessage());
        }

    }
}
