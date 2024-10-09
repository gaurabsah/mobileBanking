package com.bank.mobileBanking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "txn_tbl")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "txn_id",nullable = false)
    private String txnId;

    @Column(name = "txn_amount",nullable = false)
    private Double amount;

    @Column(name = "paid_to",nullable = false)
    private String paidTo;
}
