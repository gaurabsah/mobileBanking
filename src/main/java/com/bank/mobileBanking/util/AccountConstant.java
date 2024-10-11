package com.bank.mobileBanking.util;

public class AccountConstant {

    public static final String INSERT_ACCOUNT_DETAIL = "INSERT into account_tbl (account_holder_name, account_number,account_type, bank_balance, security_pin, bank_id) VALUES(?,?, ?, ?, ?, ?)";
    public static final String GET_ACCOUNT_BALANCE = "SELECT account_holder_name,account_number,account_type,bank_balance FROM account_tbl where account_number = ?";
    public static final String GET_ACCOUNT_SECURITY_PIN = "SELECT security_pin FROM account_tbl where account_number = ?";
    public static final String GET_ACCOUNT_DETAIL = "SELECT a.account_holder_name, a.account_number,account_type, a.bank_balance, a.security_pin, \n" +
            "b.bank_name, b.branch, b.ifsc_code\n" +
            "FROM account_tbl a\n" +
            "JOIN bank_tbl b ON a.bank_id = b.id\n" +
            "WHERE a.account_number = ?";
    public static final String UPDATE_ACCOUNT_BALANCE = "UPDATE account_tbl SET bank_balance = ? WHERE account_number = ?";
}
