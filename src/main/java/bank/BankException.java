package bank;

/**
 * Ngoại lệ chung trong hệ thống ngân hàng.
 */
public class BankException extends Exception {
  public BankException(String message) {
    super(message);
  }
}
