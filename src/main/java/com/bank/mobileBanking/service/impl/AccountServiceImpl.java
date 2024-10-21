package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.QrCodeDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.User;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
import com.bank.mobileBanking.exception.WrongSecurityPinException;
import com.bank.mobileBanking.service.AccountService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;

//    @Override
//    public Map<String, Object> createAccount(AccountDTO accountDTO) {
//        Map<String, Object> responseMap = new HashMap<>();
//        accountDAO.createAccount(accountDTO);
//        responseMap.put("success", "Account created successfully");
//        return responseMap;
//    }

    @Override
    public Map<String, Object> getBalance(String accountNumber, String sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        String securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin" + securityPin);
        }
        AccountDTO balance = accountDAO.getBalance(accountNumber);
        responseMap.put("Bank account balance fetched successfully", balance);
        return responseMap;
    }

    @Override
    public Map<String, Object> getAccountDetail(String accountNumber, String sPin) {
        Map<String, Object> responseMap = new HashMap<>();
        String securityPin = accountDAO.getSecurityPin(accountNumber);
        if (!sPin.equals(securityPin)) {
            throw new WrongSecurityPinException("Wrong Security Pin" + securityPin);
        }
        AccountDTO account = accountDAO.getAccount(accountNumber);
        responseMap.put("account detail", account);
        return responseMap;
    }

    @Override
    public byte[] generateQRCode(String accountNumber, String securityPin, int width, int height) throws IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hintsMap = new HashMap<>();
        hintsMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix;
        try {

            if (accountNumber.isEmpty()) {
                throw new ResourcesNotFoundException("Account Number is empty/null");
            }

            AccountDTO account = accountDAO.getAccount(accountNumber);
            if (account == null) {
                throw new ResourcesNotFoundException("Account Not Found");
            }

            if (!securityPin.equalsIgnoreCase(account.getPin())) {
                throw new WrongSecurityPinException("Wrong Security Pin");
            }

            String firstName = account.getUserDTO().getFirstName();
            String lastName = account.getUserDTO().getLastName();

            String name = firstName + " " + lastName;

            bitMatrix = qrCodeWriter.encode(
                    QrCodeDTO.builder()
                            .name(name)
                            .accountNumber(accountNumber)
                            .accountType(account.getAccountType().toString())
//                            .balance(1000.0)
                            .build().toString(),
                    BarcodeFormat.QR_CODE, width, height, hintsMap);

//            if u want to store qr-code in DB then you can store it -- example
//            QRTicket qrTicket = new QRTicket();
//            qrTicket.setId(UUID.randomUUID().toString());
//            qrTicket.setAccountNumber(accountNumber);
//            qrTicket.setAccountType(account.getAccountType());
//            qrTicket.setBalance(account.getBalance());
//            qrTicketRepository.save(qrTicket);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code image.", e);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage qrImage = toBufferedImage(bitMatrix);
        try {
            ImageIO.write(qrImage, "png", outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write QR code image to output stream.", e);
        }

        return outputStream.toByteArray();
    }

    private BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x, y)) {
                    graphics.fillRect(x, y, 1, 1);
                }
            }
        }
        return image;
    }


}
