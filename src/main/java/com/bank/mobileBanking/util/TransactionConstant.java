package com.bank.mobileBanking.util;

public class TransactionConstant {

    public static final String TRANSFER_MONEY = "INSERT into txn_tbl " +
            "(txn_id,txn_amount,sender_account_number,receiver_account_number,txn_date_time,service,gst,commission) " +
            "VALUES(?,?,?,?,?,?,?,?)";

    public static final String GET_TXN_DETAIL_BY_ID = "SELECT * from txn_tbl where txn_id = ?";

    public static final String GET_ALL_TXN_DETAILS = "SELECT * from txn_tbl where sender_account_number = ? " +
            "ORDER BY txn_date_time DESC";

    public static final String GET_ALL_TXNS = "SELECT *\n" +
            "FROM txn_tbl\n" +
            "WHERE sender_account_number = ?\n" +
            "AND transaction_date BETWEEN ? AND ?\n" +
            "ORDER BY transaction_date DESC";
}
