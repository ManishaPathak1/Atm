package com.progressoft.induction.atm;

import com.progressoft.induction.atm.Impl.ATMImpl;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;

import java.math.BigDecimal;
import java.util.*;

public class Main {
    public static void main(String[] args) {

    }{
        ATM atm = new ATMImpl();

        // Account number for testing
        String accountNumber = "123456789";

        // Check account balance
        BigDecimal balance = atm.checkBalance(accountNumber);
        System.out.println("Account balance: " + balance);

        // Withdraw money
        BigDecimal withdrawalAmount = new BigDecimal("250.0");
        try {
            List<Banknote> withdrawalBanknotes = atm.withdraw(accountNumber, withdrawalAmount);
            System.out.println("Withdrawal successful. Banknotes dispensed: " + withdrawalBanknotes);
        } catch (InsufficientFundsException e) {
            System.out.println("Insufficient funds in the account.");
        } catch (NotEnoughMoneyInATMException e) {
            System.out.println("Not enough money in the ATM.");
        }

        // Check account balance again
        balance = atm.checkBalance(accountNumber);
        System.out.println("Updated account balance: " + balance);
    }
}
