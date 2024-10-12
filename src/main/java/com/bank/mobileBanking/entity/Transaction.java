package com.bank.mobileBanking.entity;

import com.bank.mobileBanking.entity.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "txn_tbl")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "txn_id", nullable = false)
    private String txnId;

    @Column(name = "txn_amount", nullable = false)
    private Double amount;

    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @Column(name = "receiver_account_number", nullable = false)
    private String receiverAccountNumber;

    @CreationTimestamp
    @Column(name = "txn_date_time", nullable = false)
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "service", nullable = false)
    private ServiceType service;

    @Column(name = "gst", nullable = false)
    private Double gst;

    @Column(name = "commission", nullable = false)
    private Double commission;
}
