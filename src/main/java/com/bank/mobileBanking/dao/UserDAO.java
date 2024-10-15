package com.bank.mobileBanking.dao;

import com.bank.mobileBanking.dto.UserDTO;

public interface UserDAO {

    void register(UserDTO userDTO);

    UserDTO getUser(String email);

    boolean userExists(String email, String mobileNumber);

    boolean userExistsByEmail(String email);

    UserDTO getUserByMobileNumber(String mobileNumber);

    void updateUserLogin(UserDTO user);
}
