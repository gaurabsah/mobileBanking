package com.bank.mobileBanking.util;

public class UserConstant {
    public static final String INSERT_USER_DETAIL = "INSERT INTO user_tbl " +
            "(first_name, last_name, email, password, dob, mobile_number, pan_card_number, address, pin_code, state, country, is_logged) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_USER_DETAIL = "SELECT * from user_tbl where email = ?";
    public static final String IS_USER_EXISTS = "SELECT COUNT(*) FROM user_tbl WHERE email = ? OR mobile_number = ?";
    public static final String IS_USER_EXISTS_BY_EMAIL = "SELECT COUNT(*) FROM user_tbl WHERE email = ?";
    public static final String GET_USER_BY_MOBILE = "SELECT * FROM user_tbl where mobile_number = ?";

    public static final String UPDATE_USER_LOGGED = "UPDATE user_tbl SET is_logged = ? WHERE email = ?";
}
