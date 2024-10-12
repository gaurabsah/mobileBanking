package com.bank.mobileBanking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_tbl")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "DOB", nullable = false)
    private LocalDate dob;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "pan_card_number", nullable = false)
    private String pan;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Account account;
}
