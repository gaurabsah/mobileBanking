package com.bank.mobileBanking.util;

import java.time.Year;

public class AccountUtil {

    public static String generateAccountNumber() {
        /**
         * 2024 + randomSixDigitsNumber
         */

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

//    generate a random number between min and max

        int randomNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

//    convert currentYear & randomNumber to String then concatenate them together

        String year = String.valueOf(randomNumber);
        String randomNum = String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(year).append(randomNum);
        return accountNumber.toString();
    }
}
