package com.bank.mobileBanking.util;

public class AccountConstant {

    public static final String OPEN_ACCOUNT = "INSERT into account_tbl ( account_number,account_type, bank_balance, security_pin, bank_id, user_id) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String GET_ACCOUNT_BALANCE = "SELECT account_holder_name,account_number,account_type,bank_balance FROM account_tbl where account_number = ?";
    public static final String GET_ACCOUNT_SECURITY_PIN = "SELECT security_pin FROM account_tbl where account_number = ?";
    public static final String GET_ACCOUNT_DETAIL = "SELECT " +
            "a.account_number, a.account_type, a.bank_balance, a.security_pin, " +
            "b.bank_name, b.branch, b.ifsc_code, " +
            "u.id, u.first_name, u.last_name, u.email, " +
            "u.mobile_number, u.pan_card_number, u.address, " +
            "u.pin_code, u.state, u.country, u.id AS user_id " +
            "FROM account_tbl a " +
            "JOIN bank_tbl b ON a.bank_id = b.id " +
            "JOIN user_tbl u ON a.user_id = u.id " +
            "WHERE a.account_number = ?";

    public static final String UPDATE_ACCOUNT_BALANCE = "UPDATE account_tbl SET bank_balance = ? WHERE account_number = ?";

}
