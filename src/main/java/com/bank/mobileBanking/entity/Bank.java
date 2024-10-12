package com.bank.mobileBanking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "bank_tbl")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "branch", nullable = false)
    private String branch;

    @Column(name = "ifsc_code", nullable = false)
    private String ifsc;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bank", orphanRemoval = true)
    private List<Account> accountList = new ArrayList<>();

}
