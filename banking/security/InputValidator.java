package com.banking.security;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^ACC\\d{6}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
    
    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }
    
    public static boolean isValidAmount(String amount) {
        if (amount == null) return false;
        if (!AMOUNT_PATTERN.matcher(amount).matches()) return false;
        
        try {
            double value = Double.parseDouble(amount);
            return value > 0 && value <= 1000000; // Reasonable limit
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String sanitizeInput(String input) {
        if (input == null) return null;
        // Remove potential XSS vectors
        return input.replaceAll("[<>\"']", "");
    }
}