package com.progressoft.induction.atm.Impl;

import com.progressoft.induction.atm.ATM;
import com.progressoft.induction.atm.Banknote;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM {
    private final BankingSystemImpl bankingSystem=new BankingSystemImpl();
    private final Map<Banknote, Integer> atmCashMap = new EnumMap<>(Banknote.class);

    public ATMImpl() {
        atmCashMap.put(Banknote.FIFTY_JOD, 10);
        atmCashMap.put(Banknote.TWENTY_JOD, 20);
        atmCashMap.put(Banknote.TEN_JOD, 100);
        atmCashMap.put(Banknote.FIVE_JOD, 100);
    }

    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
        BigDecimal accountBalance = bankingSystem.getAccountBalance(accountNumber);

        if (accountBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountNumber);
        }

        List<Banknote> withdrawalBanknotes = new ArrayList<>();
        BigDecimal remainingAmount = amount;

        List<Banknote> sortedBanknotes = new ArrayList<>();
        sortedBanknotes.add(Banknote.FIFTY_JOD);
        sortedBanknotes.add(Banknote.TWENTY_JOD);
        sortedBanknotes.add(Banknote.TEN_JOD);
        sortedBanknotes.add(Banknote.FIVE_JOD);
        Collections.reverse(sortedBanknotes); // Start from the largest denomination

        for (Banknote banknote : sortedBanknotes) {
            BigDecimal banknoteValue = banknote.getValue();
            int banknotesCount = remainingAmount.divideToIntegralValue(banknoteValue).intValue();
            int availableBanknotes = atmCashMap.get(banknote);

            int countToWithdraw = Math.min(banknotesCount, availableBanknotes);
            if (countToWithdraw > 0) {
                for (int i = 0; i < countToWithdraw; i++) {
                    withdrawalBanknotes.add(banknote);
                    remainingAmount = remainingAmount.subtract(banknoteValue);
                }
                atmCashMap.put(banknote, availableBanknotes - countToWithdraw);
            }
        }

        if (remainingAmount.compareTo(BigDecimal.ZERO) != 0) {
            throw new NotEnoughMoneyInATMException(accountNumber);
        }

        bankingSystem.debitAccount(accountNumber, amount);

        return withdrawalBanknotes;
    }

    @Override
    public BigDecimal checkBalance(String accountNumber) {
        return bankingSystem.getAccountBalance(accountNumber);
    }
}