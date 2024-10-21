package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.TransactionDAO;
import com.bank.mobileBanking.dao.helper.TransactionDAOHelper;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.QrCodeDTO;
import com.bank.mobileBanking.dto.TransactionDTO;
import com.bank.mobileBanking.entity.enums.ServiceType;
import com.bank.mobileBanking.exception.AccountBalanceException;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.WrongSecurityPinException;
import com.bank.mobileBanking.service.TransactionService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    private final AccountDAO accountDAO;

    private final TransactionDAOHelper transactionDAOHelper;


    @Transactional
    @Override
    public Map<String, Object> transferMoney(TransactionDTO transactionDTO, String sPin) {
        log.info("inside transferMoney()");
        Map<String, Object> responseMap = new HashMap<>();
        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }
        String securityPin = accountDAO.getSecurityPin(transactionDTO.getSenderAccountNumber());
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
    public Map<String, Object> getTransaction(String txnId, String sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }
        String accountNumber = transactionDAOHelper.getAccountNumberByTxnId(txnId);
        if (accountNumber == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        String securityPin = accountDAO.getSecurityPin(accountNumber);
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
    public Map<String, Object> getTransactionHistory(String accountNumber, String sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        List<TransactionDTO> transactionDTOList = new ArrayList<>();

        if (sPin == null) {
            throw new IllegalArgumentException("Security PIN cannot be null");
        }

        if (accountNumber == null) {
            throw new ResourcesNotFoundException("Account Number not found");
        }

        String securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin");
        }

        transactionDTOList = transactionDAO.txnHistory(accountNumber);

        responseMap.put("Payment History", transactionDTOList);

        return responseMap;
    }

    @Transactional
    @Override
    public Map<String, Object> readQrCodeAndPay(String senderAccountNumber, Double amount, String securityPin, MultipartFile file) {
        log.info("inside readQrCodeAndPay()");
        Map<String, Object> responseMap = new HashMap<>();
//        validate senderAccountNumber
        if (senderAccountNumber == null || senderAccountNumber.isEmpty()) {
            throw new ResourcesNotFoundException("Account is null");
        }

        AccountDTO accountDTO = accountDAO.getAccount(senderAccountNumber);
        if (accountDTO == null) {
            throw new ResourcesNotFoundException("Account is null");
        }

//        read the QR-Code
        QrCodeDTO data = new QrCodeDTO();
        try {
            data = read(file);
        } catch (IOException | NotFoundException e) {
            log.error("QR-Code Data: {}", data);
            throw new RuntimeException(e);
        }

        String accountNumber = data.getAccountNumber();

        AccountDTO account = accountDAO.getAccount(accountNumber);

        if (account == null) {
            throw new ResourcesNotFoundException("Error while scanning QR-Code");
        }

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSenderAccountNumber(senderAccountNumber);
        transactionDTO.setReceiverAccountNumber(accountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setService(ServiceType.QRCODE);

        transferMoney(transactionDTO, securityPin);
        responseMap.put("success", "Transferred Successfully");
        return responseMap;
    }

    public QrCodeDTO read(final MultipartFile file) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new ResourcesNotFoundException("QR Code image could not be read.");
        }

        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

        Result result;
        try {
            result = new MultiFormatReader().decode(binaryBitmap);
        } catch (Exception e) {
            throw new ResourcesNotFoundException("QR Code could not be decoded.");
        }

        String decodedText = result.getText();
        System.out.println("Decoded text: " + decodedText);

        QrCodeDTO qrCodeDTO;
        // If the format is like "QrCodeDTO(name=Gaurav Sah, accountNumber=411277411277, accountType=SAVING)"
        if (decodedText.startsWith("QrCodeDTO(") && decodedText.endsWith(")")) {
            String[] parts = decodedText.substring(10, decodedText.length() - 1).split(", ");
            qrCodeDTO = new QrCodeDTO();

            for (String part : parts) {
                String[] keyValue = part.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];

                    switch (key) {
                        case "name":
                            qrCodeDTO.setName(value);
                            break;
                        case "accountNumber":
                            qrCodeDTO.setAccountNumber(value);
                            break;
                        case "accountType":
                            qrCodeDTO.setAccountType(value);
                            break;
                    }
                }
            }
        } else {
            throw new IOException("Decoded text is not in the expected format.");
        }

        return qrCodeDTO;
    }


    @Override
    public Map<String, Object> generateStatement(String accountNumber, String startDate, String endDate, String sPin) {
        Map<String, Object> response = new HashMap<>();

        // Validate the input parameters
        if (accountNumber == null || startDate == null || endDate == null || sPin == null) {
            response.put("status", "error");
            response.put("message", "All parameters must be provided.");
            return response;
        }

        // Validate security PIN (this may involve checking against a stored value)
        boolean isPinValid = validatePin(accountNumber, sPin);
        if (!isPinValid) {
            response.put("status", "error");
            response.put("message", "Invalid security PIN.");
            return response;
        }

        // Retrieve statement data (you would typically call a service or repository here)
        List<TransactionDTO> transactions;
        try {
            transactions = transactionDAO.getAllTransactions(accountNumber, startDate, endDate);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error retrieving transaction data: " + e.getMessage());
            return response;
        }

        // Format the response
        response.put("status", "success");
        response.put("accountNumber", accountNumber);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("transactions", transactions);

        return response;
    }

    // validation method for security PIN
    private boolean validatePin(String accountNumber, String sPin) {
        String securityPin = accountDAO.getSecurityPin(accountNumber);
        if (securityPin==null || sPin==null){
            throw new WrongSecurityPinException("Wrong Security Pin");
        }
        return sPin.equals(securityPin);
    }

}
