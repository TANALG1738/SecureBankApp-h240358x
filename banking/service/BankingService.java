package com.banking.service;

import com.banking.model.*;
import com.banking.security.PasswordHasher;
import com.banking.security.InputValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BankingService {
    private List<User> users;
    private List<Account> accounts;
    private List<Transaction> transactions;
    private FileDataService dataService;
    private User currentUser;
    
    public BankingService() {
        this.dataService = new FileDataService();
        loadAllData();
    }
    
    private void loadAllData() {
        this.users = dataService.loadUsers();
        this.accounts = dataService.loadAccounts();
        this.transactions = dataService.loadTransactions();
    }
    
    private void saveAllData() {
        dataService.saveUsers(users);
        dataService.saveAccounts(accounts);
        dataService.saveTransactions(transactions);
    }
    
    public boolean registerUser(String username, String password) {
        // Input validation
        if (!InputValidator.isValidUsername(username) || 
            !InputValidator.isValidPassword(password)) {
            return false;
        }
        
        // Check if username already exists
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            return false;
        }
        
        // Create new user with hashed password
        String salt = PasswordHasher.generateSalt();
        String passwordHash = PasswordHasher.hashPassword(password, salt);
        User newUser = new User(username, passwordHash, salt);
        
        users.add(newUser);
        saveAllData();
        return true;
    }
    
    public boolean login(String username, String password) {
        // Input validation
        if (!InputValidator.isValidUsername(username)) {
            return false;
        }
        
        Optional<User> userOpt = users.stream()
            .filter(u -> u.getUsername().equals(username) && u.isActive())
            .findFirst();
            
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (PasswordHasher.verifyPassword(password, user.getSalt(), user.getPasswordHash())) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public String createAccount(Account.AccountType type) {
        if (currentUser == null) {
            throw new IllegalStateException("User not logged in");
        }
        
        String accountNumber = generateAccountNumber();
        Account newAccount = new Account(accountNumber, currentUser.getUsername(), type);
        accounts.add(newAccount);
        saveAllData();
        
        return accountNumber;
    }
    
    private String generateAccountNumber() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 6-digit number
        return "ACC" + number;
    }
    
    public Optional<Account> getAccount(String accountNumber) {
        if (currentUser == null) {
            return Optional.empty();
        }
        
        return accounts.stream()
            .filter(a -> a.getAccountNumber().equals(accountNumber) && 
                        a.getUsername().equals(currentUser.getUsername()))
            .findFirst();
    }
    
    public boolean deposit(String accountNumber, double amount) {
        if (currentUser == null || amount <= 0) {
            return false;
        }
        
        Optional<Account> accountOpt = getAccount(accountNumber);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.deposit(amount);
            
            // Record transaction
            String transactionId = "TXN" + System.currentTimeMillis();
            Transaction transaction = new Transaction(transactionId, accountNumber, 
                Transaction.TransactionType.DEPOSIT, amount, "Deposit");
            transactions.add(transaction);
            
            saveAllData();
            return true;
        }
        return false;
    }
    
    public boolean withdraw(String accountNumber, double amount) {
        if (currentUser == null || amount <= 0) {
            return false;
        }
        
        Optional<Account> accountOpt = getAccount(accountNumber);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            boolean success = account.withdraw(amount);
            
            if (success) {
                // Record transaction
                String transactionId = "TXN" + System.currentTimeMillis();
                Transaction transaction = new Transaction(transactionId, accountNumber, 
                    Transaction.TransactionType.WITHDRAWAL, amount, "Withdrawal");
                transactions.add(transaction);
                
                saveAllData();
            }
            return success;
        }
        return false;
    }
    
    public List<Account> getUserAccounts() {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        List<Account> userAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getUsername().equals(currentUser.getUsername())) {
                userAccounts.add(account);
            }
        }
        return userAccounts;
    }
    
    public List<Transaction> getAccountTransactions(String accountNumber) {
        if (currentUser == null) {
            return new ArrayList<>();
        }
        
        // Verify the account belongs to current user
        Optional<Account> accountOpt = getAccount(accountNumber);
        if (!accountOpt.isPresent()) {
            return new ArrayList<>();
        }
        
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber)) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }
}