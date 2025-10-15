package com.banking.service;

import com.banking.model.User;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.security.InputValidator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataService {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String ACCOUNTS_FILE = DATA_DIR + "/accounts.txt";
    private static final String TRANSACTIONS_FILE = DATA_DIR + "/transactions.txt";
    
    static {
        // Create data directory if it doesn't exist
        new File(DATA_DIR).mkdirs();
    }
    
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    User user = User.fromString(InputValidator.sanitizeInput(line));
                    if (user != null) {
                        users.add(user);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }
    
    public void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.println(user.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    public List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Account account = Account.fromString(InputValidator.sanitizeInput(line));
                    if (account != null) {
                        accounts.add(account);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
        return accounts;
    }
    
    public void saveAccounts(List<Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account account : accounts) {
                writer.println(account.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }
    
    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Transaction transaction = Transaction.fromString(InputValidator.sanitizeInput(line));
                    if (transaction != null) {
                        transactions.add(transaction);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
        return transactions;
    }
    
    public void saveTransactions(List<Transaction> transactions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            for (Transaction transaction : transactions) {
                writer.println(transaction.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
        }
    }
}