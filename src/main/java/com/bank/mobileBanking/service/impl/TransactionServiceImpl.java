package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.exception.AccountBalanceException;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.WrongSecurityPinException;
import com.bank.mobileBanking.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    private final AccountDAO accountDAO;

    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public Map<String, Object> transferMoney(TransactionDTO transactionDTO, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        Long securityPin = accountDAO.getSecurityPin(transactionDTO.getSenderAccountNumber());
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin");
        }

        AccountDTO senderAccount = accountDAO.getAccount(transactionDTO.getSenderAccountNumber());

        if (senderAccount.getAccountNumber() == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        Double currentBalance = senderAccount.getBalance();
        if (currentBalance < transactionDTO.getAmount()) {
            throw new AccountBalanceException("transaction balance is greater than sender account balance");
        }

        Double newBalance = currentBalance - transactionDTO.getAmount();
        senderAccount.setBalance(newBalance);

        // Update the sender's balance in the database
        accountDAO.updateAccountBalance(senderAccount);


        AccountDTO receiverAccount = accountDAO.getAccount(transactionDTO.getReceiverAccountNumber());
        if (receiverAccount.getAccountNumber() == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        Double receiverAccountBalance = receiverAccount.getBalance();
        Double updatedBalance = receiverAccountBalance+transactionDTO.getAmount();
        receiverAccount.setBalance(updatedBalance);

        // Update the sender's balance in the database
        accountDAO.updateAccountBalance(receiverAccount);

        transactionDAO.transferMoney(transactionDTO);
        responseMap.put("success", "Amount transferred successfully");
        return responseMap;
    }
}
