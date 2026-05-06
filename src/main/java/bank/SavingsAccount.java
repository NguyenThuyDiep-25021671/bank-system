package main.java.bank;

import bank.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm - Lớp này thực thi các quy định về rút tiền và nạp tiền.
 */
public class SavingsAccount extends Account {

  // Khởi tạo Logger chuyên nghiệp thay cho System.out
  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

  // Giải quyết triệt để Magic Numbers bằng Hằng số
  private static final double MAX_WITHDRAW_AMOUNT = 1000.0;
  private static final double MIN_BALANCE = 5000.0;

  /**
   * Khởi tạo tài khoản tiết kiệm.
   *
   * @param accountNumber Số tài khoản.
   * @param balance       Số dư ban đầu.
   */
  public SavingsAccount(long accountNumber, double balance) {
    // Sửa lỗi biến tối nghĩa: Dùng accountNumber và balance
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    logger.info("Bắt đầu xử lý giao dịch nạp tiền cho tài khoản tiết kiệm: {}", getAccountNumber());
    double initialBalance = getBalance();

    try {
      doDepositing(amount);
      double finalBalance = getBalance();

      // Sử dụng hằng số từ class Transaction thay vì số '3'
      Transaction transaction = new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance
      );
      addTransaction(transaction);

      logger.info("Nạp tiền vào tài khoản {} thành công: {}", getAccountNumber(), amount);

    } catch (InvalidFundingAmountException e) {
      // Catch đích danh Exception cụ thể, ghi log kèm thông báo lỗi
      logger.error("Lỗi nạp tiền tài khoản {}: {}", getAccountNumber(), e.getMessage());
    }
  }

  @Override
  public void withdraw(double amount) {
    logger.info("Bắt đầu xử lý giao dịch rút tiền cho tài khoản tiết kiệm: {}", getAccountNumber());
    double initialBalance = getBalance();

    try {
      // Đưa logic kiểm tra nghiệp vụ lên đầu (Early Return/Throw)
      if (amount > MAX_WITHDRAW_AMOUNT) {
        logger.warn("Số tiền rút {} vượt quá hạn mức {}", amount, MAX_WITHDRAW_AMOUNT);
        throw new InvalidFundingAmountException(amount);
      }

      if (initialBalance - amount < MIN_BALANCE) {
        logger.warn("Số dư không đủ để duy trì mức tối thiểu {} sau khi rút {}",
            MIN_BALANCE, amount);
        throw new InsufficientFundsException(amount);
      }

      doWithdrawing(amount);
      double finalBalance = getBalance();

      // Sử dụng hằng số từ class Transaction thay vì số '4'
      Transaction transaction = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance
      );
      addTransaction(transaction);

      logger.info("[SAVINGS] Rút {} thành công. Số dư còn: {}", amount, finalBalance);

    } catch (InvalidFundingAmountException | InsufficientFundsException e) {
      // Dùng Multi-catch block để bắt nhiều ngoại lệ nghiệp vụ cụ thể một cách gọn gàng
      logger.error("Giao dịch rút tiền bị từ chối đối với tài khoản {}: {}",
          getAccountNumber(), e.getMessage());
    }
  }
}