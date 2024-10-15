package com.bank.mobileBanking.service.impl;

import com.bank.mobileBanking.dao.AccountDAO;
import com.bank.mobileBanking.dao.UserDAO;
import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.LoginDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.exception.ResourcesNotFoundException;
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
    public Map<String, Object> openAccount(String email, AccountDTO accountDTO) {
        log.info("inside openAccount()::service");
        Map<String, Object> responseMap = new HashMap<>();

        if (email == null || email.isEmpty()) {
            responseMap.put("error", "Email is required.");
            return responseMap;
        }

        if (accountDTO == null) {
            responseMap.put("error", "Account details are required.");
            return responseMap;
        }

        // Check if the user exists
        if (!userDAO.userExistsByEmail(email)) {
            responseMap.put("error", "User does not exist.");
            return responseMap;
        }

        // Check if the account type is valid
        if (accountDTO == null || accountDTO.toString().isEmpty()) {
            responseMap.put("error", "Account type is required.");
            return responseMap;
        }

        // Proceed to open the account
        boolean isAccountOpened = accountDAO.openAccount(email, accountDTO);
        if (isAccountOpened) {
            responseMap.put("success", "Account opened successfully.");
        } else {
            responseMap.put("error", "Failed to open the account. Please try again.");
        }

        return responseMap;
    }

    /*
    login(LoginDTO loginDTO)
    user to login
    if user has a bank account then user can login using either the combination of (email,password) or
    (email,securityPin) or (mobileNumber,password) or (mobileNumber,securityPin)
    else user need to login using (email,password) or (mobileNumber,password)
     */
    @Transactional
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        log.info("Inside login()::service");
        Map<String, Object> responseMap = new HashMap<>();

        String email = loginDTO.getEmail();
        String mobileNumber = loginDTO.getMobileNumber();
        String password = loginDTO.getPassword();

        if (!email.contains("@") && mobileNumber.length() != 10) {
            throw new SomeThingWentWrongException("Invalid Email/Mobile Number");
        }

        if (isEmpty(email) && isEmpty(mobileNumber)) {
            log.info("email: " + email + " mobile: " + mobileNumber);
            responseMap.put("error", "Email or mobile number is required.");
            return responseMap;
        }

        try {
            if (authenticateUser(email, password) || authenticateUser(mobileNumber, password)) {
                log.info("Login successful!");
                responseMap.put("success", "Login successful!");
            } else {
                log.error("invalid credential");
                responseMap.put("error", "Invalid credentials.");
            }
        } catch (Exception e) {
            log.error("Error while logging in: {}", e.getMessage());
            responseMap.put("error", "An error occurred during login.");
        }

        return responseMap;
    }

    private UserDTO getUserDetails(String identifier) {
        return isEmail(identifier) ? userDAO.getUser(identifier) : userDAO.getUserByMobileNumber(identifier);
    }

    private boolean authenticateUser(String identifier, String password) {
        log.info("inside authenticateUser()");
        UserDTO userDTO = getUserDetails(identifier);
        if (userDTO != null) {
            if (!userDTO.getIsLogged()) {
                if ((identifier.equalsIgnoreCase(userDTO.getEmail())) || (identifier.equalsIgnoreCase(userDTO.getMobileNumber()))) {
                    log.info("email or mobileNumber matched in DB ");
                    if (userDTO.getAccountDTO() == null) {
                        return loginWithPassword(identifier, password);
                    } else {
                        return loginWithPassword(identifier, password) || loginWithSecurityPin(identifier, userDTO.getAccountDTO().getPin());
                    }
                }
            }

        }
        return false;
    }

    private boolean loginWithPassword(String identifier, String password) {
        log.info("inside loginWithPassword()");
        try {
            UserDTO user = new UserDTO();
            // Fetch user details from the database
            if (identifier.contains("@")) {
                user = userDAO.getUser(identifier);
            } else {
                user = userDAO.getUserByMobileNumber(identifier);
            }


            // Verify the password
            if (user != null && (password.equalsIgnoreCase(user.getPassword()))) {
                log.info("password: "+password+" || expected pswd: "+user.getPassword());
                user.setIsLogged(true);
                userDAO.updateUserLogin(user);
                return true; // Login successful
            } else {
                log.warn("Invalid credentials for user: {}", identifier);
                return false; // Invalid credentials
            }

        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage());
            return false;
        }
    }

    private boolean loginWithSecurityPin(String identifier, String securityPin) {

        log.info("inside loginWithSecurityPin()");
        try {
            UserDTO user = new UserDTO();
            // Fetch user details from the database
            if (identifier.contains("@")) {
                user = userDAO.getUser(identifier);
            } else {
                user = userDAO.getUserByMobileNumber(identifier);
            }

            // Verify the security pin
            if (user != null && (securityPin.equalsIgnoreCase(user.getAccountDTO().getPin()))) {
                user.setIsLogged(true);
                userDAO.updateUserLogin(user);
                return true; // Login successful
            } else {
                log.warn("Invalid credentials for user: {}", identifier);
                return false; // Invalid credentials
            }

        } catch (Exception e) {
            log.error("Unexpected error during login: {}", e.getMessage());
            return false;
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isEmail(String identifier) {
        return identifier != null && identifier.contains("@");
    }

    /*
    register(UserDTO userDTO)
    User to register to mobileBanking
     */
    @Override
    public Map<String, Object> register(UserDTO userDTO) {
        Map<String, Object> responseMap = new HashMap<>();

        // Validate the userDTO object
        if (userDTO == null) {
            throw new ResourcesNotFoundException("User Details Not Found");
        }

        // Validate required fields
        if (isEmpty(userDTO.getEmail())) {
            responseMap.put("error", "Email is required.");
            return responseMap;
        }

        if (isEmpty(userDTO.getMobileNumber())) {
            responseMap.put("error", "Mobile Number is required.");
            return responseMap;
        }

        if (isEmpty(userDTO.getPassword())) {
            responseMap.put("error", "Password is required.");
            return responseMap;
        }

        if (!userDTO.getEmail().contains("@")) {
            throw new SomeThingWentWrongException("Invalid Email");
        }

        if (userDTO.getMobileNumber().length() != 10) {
            throw new SomeThingWentWrongException("Invalid Mobile Number");
        }

        // Password complexity check
        if (userDTO.getPassword().length() < 8) {
            responseMap.put("error", "Password must be at least 8 characters long.");
            return responseMap;
        }

        // Check for existing user
        if (userDAO.userExists(userDTO.getEmail(), userDTO.getMobileNumber())) {
            responseMap.put("error", "User already exists with this email or mobile number.");
            return responseMap;
        }

        userDTO.setIsLogged(false);

        // Save the user
        userDAO.register(userDTO);

        responseMap.put("success", "Registered Successfully");
        return responseMap;
    }


}
