package com.bank.mobileBanking.util;

public class TransactionConstant {

    public static final String TRANSFER_MONEY = "INSERT into txn_tbl (txn_id,txn_amount,sender_account_number,receiver_account_number) VALUES(?,?,?,?)";
}
