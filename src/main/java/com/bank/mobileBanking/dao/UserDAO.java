package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.UserDTO;

public interface UserDAO {

    void register(UserDTO userDTO);

    UserDTO getUser(String email);
}
