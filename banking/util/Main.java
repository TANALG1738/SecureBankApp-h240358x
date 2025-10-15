package com.banking.util;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.security.InputValidator;
import com.banking.service.BankingService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static BankingService bankingService;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        bankingService = new BankingService();
        scanner = new Scanner(System.in);
        
        System.out.println("=== Secure Banking Application ===");
        
        try {
            boolean running = true;
            while (running) {
                if (bankingService.getCurrentUser() == null) {
                    running = showMainMenu();
                } else {
                    running = showUserMenu();
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred. Please try again.");
        } finally {
            scanner.close();
            System.out.println("Thank you for using our banking service!");
        }
    }
    
    private static boolean showMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                handleLogin();
                break;
            case "2":
                handleRegistration();
                break;
            case "3":
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
    
    private static boolean showUserMenu() {
        System.out.println("\n=== User Menu ===");
        System.out.println("Welcome, " + bankingService.getCurrentUser().getUsername() + "!");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. View Transaction History");
        System.out.println("6. Logout");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                handleCreateAccount();
                break;
            case "2":
                handleViewAccounts();
                break;
            case "3":
                handleDeposit();
                break;
            case "4":
                handleWithdraw();
                break;
            case "5":
                handleViewTransactions();
                break;
            case "6":
                bankingService.logout();
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return true;
    }
    
    private static void handleLogin() {
        System.out.print("Enter username: ");
        String username = InputValidator.sanitizeInput(scanner.nextLine().trim());
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // Don't sanitize passwords
        
        try {
            if (bankingService.login(username, password)) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (Exception e) {
            System.out.println("Login failed. Please try again.");
        }
    }
    
    private static void handleRegistration() {
        System.out.print("Enter username (3-20 alphanumeric characters): ");
        String username = InputValidator.sanitizeInput(scanner.nextLine().trim());
        
        System.out.print("Enter password (min 8 characters): ");
        String password = scanner.nextLine(); // Don't sanitize passwords
        
        try {
            if (bankingService.registerUser(username, password)) {
                System.out.println("Registration successful! You can now login.");
            } else {
                System.out.println("Registration failed. Username may be taken or invalid.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed. Please try again.");
        }
    }
    
    private static void handleCreateAccount() {
        System.out.println("Select account type:");
        System.out.println("1. Savings");
        System.out.println("2. Checking");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        Account.AccountType type;
        
        switch (choice) {
            case "1":
                type = Account.AccountType.SAVINGS;
                break;
            case "2":
                type = Account.AccountType.CHECKING;
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }
        
        try {
            String accountNumber = bankingService.createAccount(type);
            System.out.println("Account created successfully!");
            System.out.println("Your account number: " + accountNumber);
        } catch (Exception e) {
            System.out.println("Failed to create account. Please try again.");
        }
    }
    
    private static void handleViewAccounts() {
        List<Account> accounts = bankingService.getUserAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        
        System.out.println("\n=== Your Accounts ===");
        for (Account account : accounts) {
            System.out.printf("Account: %s | Type: %s | Balance: $%.2f%n",
                account.getAccountNumber(), account.getType(), account.getBalance());
        }
    }
    
    private static void handleDeposit() {
        System.out.print("Enter account number: ");
        String accountNumber = InputValidator.sanitizeInput(scanner.nextLine().trim());
        
        System.out.print("Enter amount to deposit: ");
        String amountStr = scanner.nextLine().trim();
        
        if (!InputValidator.isValidAmount(amountStr)) {
            System.out.println("Invalid amount format.");
            return;
        }
        
        double amount = Double.parseDouble(amountStr);
        
        try {
            if (bankingService.deposit(accountNumber, amount)) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Deposit failed. Please check the account number.");
            }
        } catch (Exception e) {
            System.out.println("Deposit failed. Please try again.");
        }
    }
    
    private static void handleWithdraw() {
        System.out.print("Enter account number: ");
        String accountNumber = InputValidator.sanitizeInput(scanner.nextLine().trim());
        
        System.out.print("Enter amount to withdraw: ");
        String amountStr = scanner.nextLine().trim();
        
        if (!InputValidator.isValidAmount(amountStr)) {
            System.out.println("Invalid amount format.");
            return;
        }
        
        double amount = Double.parseDouble(amountStr);
        
        try {
            if (bankingService.withdraw(accountNumber, amount)) {
                System.out.println("Withdrawal successful!");
            } else {
                System.out.println("Withdrawal failed. Check account number or insufficient funds.");
            }
        } catch (Exception e) {
            System.out.println("Withdrawal failed. Please try again.");
        }
    }
    
    private static void handleViewTransactions() {
        System.out.print("Enter account number: ");
        String accountNumber = InputValidator.sanitizeInput(scanner.nextLine().trim());
        
        List<Transaction> transactions = bankingService.getAccountTransactions(accountNumber);
        
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }
        
        System.out.println("\n=== Transaction History ===");
        for (Transaction transaction : transactions) {
            System.out.printf("%s | %s | $%.2f | %s%n",
                transaction.getTimestamp(), transaction.getType(),
                transaction.getAmount(), transaction.getDescription());
        }
    }
}