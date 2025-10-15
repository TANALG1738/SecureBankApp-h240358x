package com.banking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String accountNumber;
    private String username;
    private double balance;
    private AccountType type;
    private LocalDateTime createdAt;
    
    public enum AccountType {
        SAVINGS, CHECKING
    }
    
    public Account(String accountNumber, String username, AccountType type) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.type = type;
        this.balance = 0.0;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public AccountType getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return accountNumber + "," + username + "," + balance + "," + type + "," + createdAt;
    }
    
    public static Account fromString(String data) {
        String[] parts = data.split(",");
        if (parts.length == 5) {
            Account account = new Account(parts[0], parts[1], AccountType.valueOf(parts[3]));
            account.balance = Double.parseDouble(parts[2]);
            account.createdAt = LocalDateTime.parse(parts[4]);
            return account;
        }
        return null;
    }
}