package main.java.bank;

import bank.InvalidFundingAmountException;
import bank.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Đại diện cho một tài khoản ngân hàng cơ bản.
 * Cung cấp các thuộc tính và phương thức chung cho các loại tài khoản.
 */
public abstract class Account {

  // Sửa lỗi Hằng số: Phải viết hoa toàn bộ và phân cách bằng dấu gạch dưới
  public static final String CHECKING_TYPE = "CHECKING";
  public static final String SAVINGS_TYPE = "SAVINGS";

  // Khởi tạo Logger để ghi lại hoạt động của hệ thống
  private static final Logger logger = LoggerFactory.getLogger(Account.class);

  // Sửa lỗi Tên biến: Đổi accNum thành accountNumber, B thành balance để rõ nghĩa (camelCase)
  private long accountNumber;
  private double balance;
  protected List<Transaction> transactionList;

  /**
   * Khởi tạo tài khoản với số tài khoản và số dư ban đầu.
   *
   * @param accountNumber Số tài khoản.
   * @param balance       Số dư ban đầu.
   */
  public Account(long accountNumber, double balance) {
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.transactionList = new ArrayList<>();
  }

  // Sửa lỗi: Tách các phương thức trên 1 dòng thành nhiều dòng, thêm ngoặc nhọn
  public long getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(long accountNumber) {
    this.accountNumber = accountNumber;
  }

  public double getBalance() {
    return balance;
  }

  protected void setBalance(double balance) {
    this.balance = balance;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  /**
   * Cập nhật danh sách giao dịch của tài khoản.
   *
   * @param transactionList Danh sách giao dịch mới.
   */
  public void setTransactionList(List<Transaction> transactionList) {
    // Sửa lỗi: Thêm ngoặc nhọn cho khối lệnh if-else
    if (transactionList == null) {
      this.transactionList = new ArrayList<>();
    } else {
      this.transactionList = transactionList;
    }
  }

  /**
   * Thực hiện nạp tiền vào tài khoản.
   *
   * @param amount Số tiền cần nạp.
   */
  public abstract void deposit(double amount);

  /**
   * Thực hiện rút tiền từ tài khoản.
   *
   * @param amount Số tiền cần rút.
   */
  public abstract void withdraw(double amount);

  /**
   * Xử lý logic cộng tiền vào số dư.
   *
   * @param amount Số tiền nạp.
   * @throws InvalidFundingAmountException Nếu số tiền nạp nhỏ hoặc bằng 0.
   */
  protected void doDepositing(double amount) throws InvalidFundingAmountException {
    // Sửa lỗi: Thêm khoảng trắng quanh toán tử <=, bao bọc lệnh if bằng ngoặc nhọn
    if (amount <= 0) {
      logger.error("Lỗi nạp tiền: Số tiền {} không hợp lệ cho tài khoản {}", amount, accountNumber);
      throw new InvalidFundingAmountException(amount);
    }
    balance += amount;
    logger.info("Đã nạp thành công {} vào tài khoản {}", amount, accountNumber);
  }

  /**
   * Xử lý logic trừ tiền từ số dư.
   *
   * @param amount Số tiền rút.
   * @throws InvalidFundingAmountException Nếu số tiền rút nhỏ hoặc bằng 0.
   * @throws InsufficientFundsException    Nếu số dư không đủ để rút.
   */
  protected void doWithdrawing(double amount)
      throws InvalidFundingAmountException, InsufficientFundsException {
    // Sửa lỗi: Tung ra Exception cụ thể thay vì "throws Exception" chung chung
    if (amount <= 0) {
      logger.error("Lỗi rút tiền: Số tiền {} không hợp lệ cho tài khoản {}", amount, accountNumber);
      throw new InvalidFundingAmountException(amount);
    }
    if (amount > balance) {
      logger.error("Lỗi rút tiền: Tài khoản {} không đủ số dư (Cần rút: {}, Số dư: {})",
          accountNumber, amount, balance);
      throw new InsufficientFundsException(amount);
    }
    balance -= amount;
    logger.info("Đã rút thành công {} từ tài khoản {}", amount, accountNumber);
  }

  /**
   * Thêm một giao dịch mới vào danh sách.
   *
   * @param transaction Giao dịch cần thêm.
   */
  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactionList.add(transaction);
    }
  }

  /**
   * Lấy chuỗi chứa toàn bộ lịch sử giao dịch của tài khoản.
   *
   * @return Lịch sử giao dịch dưới dạng chuỗi văn bản.
   */
  public String getTransactionHistory() {
    // Sửa lỗi: Thay thế System.out.println bằng hệ thống Logger chuyên nghiệp
    logger.info("Đang truy xuất lịch sử giao dịch cho tài khoản: {}", accountNumber);

    // Sửa lỗi: Dùng StringBuilder để nối chuỗi trong vòng lặp nhằm tối ưu hiệu suất (Performance)
    StringBuilder history = new StringBuilder();
    history.append("Lịch sử giao dịch của tài khoản ").append(accountNumber).append(":\n");

    for (int i = 0; i < transactionList.size(); i++) {
      history.append(transactionList.get(i).getTransactionSummary());
      if (i < transactionList.size() - 1) {
        history.append("\n");
      }
    }
    return history.toString();
  }

  @Override
  public boolean equals(Object obj) {
    // Sửa lỗi: Thêm ngoặc nhọn cho các lệnh if
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Account)) {
      return false;
    }
    Account other = (Account) obj;
    return this.accountNumber == other.accountNumber;
  }

  @Override
  public int hashCode() {
    // Sửa lỗi: Sử dụng Objects.hash để code gọn gàng, an toàn và đúng chuẩn
    return Objects.hash(accountNumber);
  }
}