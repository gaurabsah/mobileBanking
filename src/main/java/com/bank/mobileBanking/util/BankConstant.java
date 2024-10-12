package com.bank.mobileBanking.util;

public class BankConstant {

    public static final String GET_BANK_DETAIL_BY_BANK_NAME = "SELECT * FROM bank_tbl WHERE bank_name = ?";
    public static final String GET_BANK_DETAIL_BY_IFSC = "SELECT * FROM bank_tbl WHERE ifsc_code = ?";
    public static final String GET_ALL_BANK_DETAIL = "SELECT * FROM bank_tbl";
    public static final String INSERT_BANK_DETAIL = "INSERT into bank_tbl (bank_name,branch,ifsc_code) values(?,?,?)";
    public static final String IS_BANK_NAME_EXISTS = "SELECT COUNT(*) FROM bank_tbl WHERE bank_name = ?";
    public static final String GET_BANK_DETAIL_BY_NAME_AND_IFSC = "SELECT * from bank_tbl WHERE bank_name = ? AND ifsc_code = ?";

    public static final String GET_BANK_ID = "SELECT id FROM bank_tbl WHERE bank_name = ? AND ifsc_code = ?";
    public static final String GET_BANK_DETAIL_BY_BRANCH = "SELECT * FROM bank_tbl WHERE branch like ?";
    public static final String GET_BANK_DETAIL_BY_NAME_AND_BRANCH = "SELECT * from bank_tbl WHERE bank_name = ? AND branch = ?";
}
