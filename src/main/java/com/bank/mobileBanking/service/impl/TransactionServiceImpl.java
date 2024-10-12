package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dao.helper.TransactionDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.exception.AccountBalanceException;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.WrongSecurityPinException;
import com.bank.mobileBanking.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    private final AccountDAO accountDAO;

    private final TransactionDAOHelper transactionDAOHelper;


    @Transactional
    @Override
    public Map<String, Object> transferMoney(TransactionDTO transactionDTO, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }
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
        Double updatedBalance = receiverAccountBalance + transactionDTO.getAmount();
        receiverAccount.setBalance(updatedBalance);

        // Update the receiver's balance in the database
        accountDAO.updateAccountBalance(receiverAccount);

        transactionDAO.transferMoney(transactionDTO);
        responseMap.put("success", "Amount transferred successfully");
        return responseMap;
    }

    @Override
    public Map<String, Object> getTransaction(String txnId, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }
        Long accountNumber = transactionDAOHelper.getAccountNumberByTxnId(txnId);
        if (accountNumber == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        Long securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin");
        }
        TransactionDTO dto = transactionDAO.getTransaction(txnId);

        if (dto == null) {
            throw new ResourcesNotFoundException("Transaction Not Found");
        }

        responseMap.put("Transaction Detail", dto);

        return responseMap;
    }

    @Override
    public Map<String, Object> getTransactionHistory(Long accountNumber, Long sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }

        if (accountNumber == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        Long securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin");
        }

        transactionDTOList = transactionDAO.txnHistory(accountNumber);

        responseMap.put("Payment History", transactionDTOList);

        return responseMap;
    }
}
