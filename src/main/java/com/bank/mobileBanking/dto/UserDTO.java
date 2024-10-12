package com.bank.mobileBanking.dto;

import com.bank.mobileBanking.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate dob;

    private String mobileNumber;

    private String pan;

    private String address;

    private String pinCode;

    private String state;

    private String country;

    private AccountDTO accountDTO;
}
