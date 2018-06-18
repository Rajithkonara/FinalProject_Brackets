package com.brackets.stockexchange.model;

import javafx.beans.DefaultProperty;
import org.springframework.boot.jackson.JsonComponent;

import javax.persistence.*;

@JsonComponent
@Entity
public class Bank {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(unique=true)
    private String account_name;
    private Long balance;
    private int turns = 0;

    public Bank() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }
}