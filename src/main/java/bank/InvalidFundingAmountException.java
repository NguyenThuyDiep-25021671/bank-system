package main.java.bank;

import bank.BankException;

import java.util.Locale;

/**
 * Ngoại lệ khi số tiền giao dịch không hợp lệ.
 */
public class InvalidFundingAmountException extends BankException {
  public InvalidFundingAmountException(double amount) {
    super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
  }
}
