package com.bank.mobileBanking.util;

public class BankConstant {

    public static final String GET_BANK_DETAIL_BY_BANK_NAME = "SELECT * FROM bank_tbl WHERE bank_name = ?";
    public static final String GET_BANK_DETAIL_BY_IFSC = "SELECT * FROM bank_tbl WHERE ifsc_code = ?";
    public static final String GET_ALL_BANK_DETAIL = "SELECT * FROM bank_tbl";
    public static final String INSERT_BANK_DETAIL = "INSERT into bank_tbl (bank_name,branch,ifsc_code) values(?,?,?)";
}
