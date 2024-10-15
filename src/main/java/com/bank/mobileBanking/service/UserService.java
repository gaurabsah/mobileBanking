package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.AccountDTO;
import com.bank.mobileBanking.dto.LoginDTO;
import com.bank.mobileBanking.dto.UserDTO;
import com.bank.mobileBanking.entity.enums.LoginType;

import java.util.Map;

public interface UserService {

    Map<String, Object> openAccount(String email, AccountDTO accountDTO);

    Map<String, Object> login(LoginDTO loginDTO);

    Map<String, Object> register(UserDTO userDTO);
}
