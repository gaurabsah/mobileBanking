package com.bank.mobileBanking.service;

import com.bank.mobileBanking.dto.UserDTO;

import java.util.Map;

public interface UserService {

    Map<String, Object> register(UserDTO userDTO);
}
