package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.UserDAO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.exception.ResourcesAlreadyExistsException;
import com.bank.mobileBanking.exception.SomeThingWentWrongException;
import com.bank.mobileBanking.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    private final AccountDAO accountDAO;


    @Transactional
    @Override
    public Map<String, Object> register(UserDTO userDTO) {
        log.info("inside register()::service");
        Map<String, Object> responseMap = new HashMap<>();

        if (userDTO == null) {
            log.info("userDTO {}", userDTO.toString());
            throw new SomeThingWentWrongException("User can't be null");
        }

        UserDTO user = userDAO.getUser(userDTO.getEmail());

        if (user != null) {
            if (userDTO.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new ResourcesAlreadyExistsException("Email Already Exists");
            }
        }

        try {
            userDAO.register(userDTO);
            log.info("User registered successfully: {}", userDTO);
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            throw new SomeThingWentWrongException("Error registering user");
        }

        log.info("assigning account number to user");

        UserDTO daoUser = userDAO.getUser(userDTO.getEmail());

        if (daoUser == null) {
            log.error("User not found after registration for email: {}", userDTO.getEmail());
            throw new SomeThingWentWrongException("User registration failed, user not found");
        }

        if (userDTO.getAccountDTO() != null) {
            try {
                accountDAO.createAccount(daoUser, userDTO.getAccountDTO());
                log.info("Account created successfully for user: {}", daoUser);
            } catch (Exception e) {
                log.error("Error creating account: {}", e.getMessage());
                throw new SomeThingWentWrongException("Error creating account");
            }

        } else {
            responseMap.put("message", "User Not registered");
        }
        responseMap.put("registeredUser", userDTO.toString());
        return responseMap;

    }

}
