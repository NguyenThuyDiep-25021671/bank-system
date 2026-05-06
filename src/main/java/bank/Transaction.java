package bank;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Đại diện cho một giao dịch ngân hàng.
 * Lưu trữ thông tin chi tiết về loại giao dịch, số tiền và biến động số dư.
 */
public class Transaction {

  // Khởi tạo Logger cho class Transaction
  private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

  public static final int TYPE_DEPOSIT_CHECKING = 1;
  public static final int TYPE_WITHDRAW_CHECKING = 2;
  public static final int TYPE_DEPOSIT_SAVINGS = 3;
  public static final int TYPE_WITHDRAW_SAVINGS = 4;

  private int type;
  private double amount;
  private double initialBalance;
  private double finalBalance;

  /**
   * Khởi tạo một giao dịch mới.
   *
   * @param type           Loại giao dịch (dựa trên các hằng số TYPE_).
   * @param amount         Số tiền giao dịch.
   * @param initialBalance Số dư trước giao dịch.
   * @param finalBalance   Số dư sau giao dịch.
   */
  public Transaction(int type, double amount, double initialBalance, double finalBalance) {
    this.type = type;
    this.amount = amount;
    this.initialBalance = initialBalance;
    this.finalBalance = finalBalance;
  }

  // Sửa lỗi: Tách các hàm getter/setter xuống dòng chuẩn Google Style
  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getInitialBalance() {
    return initialBalance;
  }

  public void setInitialBalance(double initialBalance) {
    this.initialBalance = initialBalance;
  }

  public double getFinalBalance() {
    return finalBalance;
  }

  public void setFinalBalance(double finalBalance) {
    this.finalBalance = finalBalance;
  }

  /**
   * Lấy chuỗi mô tả loại giao dịch dựa trên mã số.
   *
   * @param type Mã số loại giao dịch.
   * @return Chuỗi văn bản mô tả giao dịch.
   */
  public static String getTransactionTypeString(int type) {
    // Sửa lỗi: Sử dụng Hằng số thay cho Magic Numbers trong lệnh switch
    switch (type) {
      case TYPE_DEPOSIT_CHECKING:
        return "Nạp tiền vãng lai";
      case TYPE_WITHDRAW_CHECKING:
        return "Rút tiền vãng lai";
      case TYPE_DEPOSIT_SAVINGS:
        return "Nạp tiền tiết kiệm";
      case TYPE_WITHDRAW_SAVINGS:
        return "Rút tiền tiết kiệm";
      default:
        return "Không rõ";
    }
  }

  /**
   * Trả về chuỗi tóm tắt chi tiết của giao dịch.
   *
   * @return Thông tin giao dịch đã được định dạng.
   */
  public String getTransactionSummary() {
    // Sửa lỗi: Thay System.out bằng Logger. Ghi log ở mức DEBUG cho các tiến trình nhỏ.
    logger.debug("Bắt đầu xử lý chuỗi tóm tắt cho giao dịch loại: {}", this.type);

    /* * Sửa lỗi:
     * 1. Gộp toàn bộ vào một lệnh String.format duy nhất để tránh gọi Locale.US nhiều lần.
     * 2. Ngắt dòng (Line break) để tuân thủ quy tắc Line Length < 100 characters.
     */
    return String.format(Locale.US,
        "- Kiểu giao dịch: %s. Số dư ban đầu: $%.2f. Số tiền: $%.2f. Số dư cuối: $%.2f.",
        getTransactionTypeString(this.type),
        this.initialBalance,
        this.amount,
        this.finalBalance);
  }
}