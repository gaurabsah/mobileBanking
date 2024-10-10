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

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "bank_balance")
    private Double balance;

    @Column(name = "security_pin",nullable = false)
    private Long pin;

    @OneToOne
    @JoinColumn(name = "bank_name")
    private Bank bank;
}
