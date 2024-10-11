package com.bank.mobileBanking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

    @Column(name = "sender_account_number",nullable = false)
    private Long senderAccountNumber;

    @Column(name = "receiver_account_number",nullable = false)
    private Long receiverAccountNumber;
}
