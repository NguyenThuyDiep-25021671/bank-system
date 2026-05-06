package main.java.bank;

import bank.BankException;
import bank.Transaction;

/**
 * Tài khoản vãng lai.
 */
public class CheckingAccount extends Account {
  public CheckingAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction t = new Transaction(
          Transaction.TYPE_DEPOSIT_CHECKING,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
    } catch (BankException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  public void withdraw(double amount) {
    double initialBalance = getBalance();
    try {
      doWithdrawing(amount);
      double finalBalance = getBalance();
      Transaction t = new Transaction(
          Transaction.TYPE_WITHDRAW_CHECKING,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
    } catch (BankException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
