package com.bank.mobileBanking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "account_tbl")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_holder_name",nullable = false)
    private String accountHolderName;

    @Column(name = "account_number",nullable = true)
    private Long accountNumber;

    @Column(name = "bank_balance",nullable = true)
    private Double balance;

    @Column(name = "security_pin",nullable = false)
    private Long pin;

    @OneToOne
    private Bank bank;
}
