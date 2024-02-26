package com.progressoft.induction.atm.Impl;

import com.progressoft.induction.atm.BankingSystem;
import com.progressoft.induction.atm.Banknote;
import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class BankingSystemImpl implements BankingSystem {
    Map<String, BigDecimal> accountBalanceMap = new HashMap<String, BigDecimal>();
    EnumMap<Banknote,Integer> atmCashMap = new EnumMap<>(Banknote.class);

    public BankingSystemImpl() {
        atmCashMap.put(Banknote.FIFTY_JOD,10);
        atmCashMap.put(Banknote.TWENTY_JOD,20);
        atmCashMap.put(Banknote.TEN_JOD,100);
        atmCashMap.put(Banknote.FIVE_JOD,100);

        accountBalanceMap.put("123456789", BigDecimal.valueOf(1000.0));
        accountBalanceMap.put("111111111", BigDecimal.valueOf(1000.0));
        accountBalanceMap.put("222222222", BigDecimal.valueOf(1000.0));
        accountBalanceMap.put("333333333", BigDecimal.valueOf(1000.0));
        accountBalanceMap.put("444444444", BigDecimal.valueOf(1000.0));
    }

    public BigDecimal sumOfMoneyInAtm(){
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<Banknote, Integer> entry : atmCashMap.entrySet()) {
            sum = sum.add(entry.getKey().getValue().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return sum;
    }


    @Override
    public BigDecimal getAccountBalance(String accountNumber){
        BigDecimal balance = accountBalanceMap.get(accountNumber);
        if (balance == null) {
            throw new AccountNotFoundException("Account with account number "+accountNumber + " not found");
        }
        return balance;
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {
        BigDecimal balance = accountBalanceMap.get(accountNumber);
        if (balance == null) {
            throw new AccountNotFoundException("Account with account number "+accountNumber + " not found");
        }
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Account with account number " +accountNumber+ " does not have sufficient fund");
        }
        accountBalanceMap.put(accountNumber, newBalance);
    }
}