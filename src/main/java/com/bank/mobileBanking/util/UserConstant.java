package com.bank.mobileBanking.util;

public class UserConstant {
    public static final String INSERT_USER_DETAIL = "INSERT into user_tbl " +
            "(first_name,last_name,email,DOB,mobile_number,pan_card_number,address,pin_code,state,country) " +
            "VALUES(?,?,?,?::date,?,?,?,?,?,?)";
    public static final String GET_USER_DETAIL = "SELECT * from user_tbl where email = ?";
}
