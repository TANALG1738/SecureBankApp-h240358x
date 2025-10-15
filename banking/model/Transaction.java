package com.banking.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;
    
    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }
    
    public Transaction(String transactionId, String accountNumber, TransactionType type, 
                      double amount, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.description = description;
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    
    @Override
    public String toString() {
        return transactionId + "," + accountNumber + "," + type + "," + amount + "," + 
               timestamp + "," + description;
    }
    
    public static Transaction fromString(String data) {
        String[] parts = data.split(",", 6);
        if (parts.length == 6) {
            return new Transaction(parts[0], parts[1], TransactionType.valueOf(parts[2]), 
                                 Double.parseDouble(parts[3]), parts[5]);
        }
        return null;
    }
}