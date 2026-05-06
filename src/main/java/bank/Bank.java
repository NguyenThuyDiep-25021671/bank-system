package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Đại diện cho hệ thống ngân hàng quản lý danh sách khách hàng.
 */
public class Bank {

  // Khởi tạo Logger
  private static final Logger logger = LoggerFactory.getLogger(Bank.class);

  // Định nghĩa các hằng số (Constants) để tránh lỗi "Magic String"
  private static final String ID_REGEX = "\\d{9}";
  private static final String CHECKING = "CHECKING";
  private static final String SAVINGS = "SAVINGS";

  // Sửa lỗi tên biến: Đổi c_list thành customerList (chuẩn camelCase)
  private List<Customer> customerList;

  /**
   * Khởi tạo ngân hàng với danh sách khách hàng rỗng.
   */
  public Bank() {
    this.customerList = new ArrayList<>();
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Cập nhật danh sách khách hàng.
   *
   * @param customerList Danh sách khách hàng mới.
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
  }

  /**
   * Đọc dữ liệu khách hàng và tài khoản từ luồng đầu vào (InputStream).
   *
   * @param inputStream Luồng dữ liệu đầu vào.
   */
  public void readCustomerList(InputStream inputStream) {
    logger.info("Bắt đầu đọc dữ liệu khách hàng từ InputStream...");

    if (inputStream == null) {
      logger.warn("InputStream bị null, không thể đọc dữ liệu.");
      return;
    }

    Customer current = null;

    // Sử dụng try-with-resources để Java tự động đóng luồng (Stream) khi đọc xong
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();

        // Sửa lỗi: Dùng continue để bỏ qua dòng trống, giảm độ sâu lồng lệnh (nesting)
        if (line.isEmpty()) {
          continue;
        }

        int last = line.lastIndexOf(' ');
        if (last > 0) {
          String token = line.substring(last + 1).trim();

          // Phân nhánh logic: Nếu token là ID (9 chữ số) thì đây là khách hàng mới
          if (token.matches(ID_REGEX)) {
            String name = line.substring(0, last).trim();
            current = new Customer(Long.parseLong(token), name);
            customerList.add(current);
            logger.info("Đã thêm khách hàng mới: {}", name);
          } else if (current != null) {
            // Sửa lỗi hàm quá dài: Tách logic xử lý tài khoản ra một hàm riêng (Extract Method)
            parseAndAddAccount(current, line);
          }
        }
      }
    } catch (IOException e) {
      // Sửa lỗi: Bắt đúng IOException thay vì bắt Exception chung chung
      logger.error("Lỗi I/O khi đọc dữ liệu từ InputStream: ", e);
    } catch (NumberFormatException e) {
      logger.error("Lỗi định dạng số trong quá trình đọc file: ", e);
    }
  }

  /**
   * Hàm phụ trợ (Helper method) để phân tách chuỗi và thêm tài khoản cho khách hàng.
   * Giúp hàm readCustomerList() trở nên ngắn gọn và dễ bảo trì hơn.
   */
  private void parseAndAddAccount(Customer customer, String line) {
    String[] parts = line.split("\\s+");
    if (parts.length >= 3) {
      try {
        long num = Long.parseLong(parts[0]);
        double bal = Double.parseDouble(parts[2]);

        // Sử dụng phương thức .equals() với hằng số đặt trước để tránh lỗi NullPointerException
        if (CHECKING.equals(parts[1])) {
          customer.addAccount(new CheckingAccount(num, bal));
        } else if (SAVINGS.equals(parts[1])) {
          customer.addAccount(new SavingsAccount(num, bal));
        } else {
          logger.warn("Loại tài khoản không hợp lệ bị bỏ qua: {}", parts[1]);
        }
      } catch (NumberFormatException e) {
        logger.error("Dữ liệu tài khoản không đúng chuẩn định dạng số: {}", line);
      }
    }
  }

  /**
   * Lấy thông tin tất cả khách hàng sắp xếp theo số ID tăng dần.
   *
   * @return Chuỗi chứa thông tin khách hàng đã được sắp xếp.
   */
  public String getCustomersInfoByIdOrder() {
    // Sửa lỗi: Dùng Lambda Expression cực kỳ ngắn gọn thay cho Anonymous Class dài dòng
    return buildSortedCustomerInfo(Comparator.comparingLong(Customer::getIdNumber));
  }

  /**
   * Lấy thông tin tất cả khách hàng sắp xếp theo Tên, nếu trùng tên thì xếp theo ID.
   *
   * @return Chuỗi chứa thông tin khách hàng đã được sắp xếp.
   */
  public String getCustomersInfoByNameOrder() {
    // Sửa lỗi: Nối các bộ lọc (thenComparing) rất tinh tế và chuẩn xác
    return buildSortedCustomerInfo(
        Comparator.comparing(Customer::getFullName).thenComparingLong(Customer::getIdNumber)
    );
  }

  /**
   * Hàm phụ trợ chung xử lý việc sắp xếp và nối chuỗi thông tin khách hàng.
   * Giải quyết triệt để lỗi lặp code (Code Duplication) ở 2 hàm bên trên.
   *
   * @param comparator Tiêu chí dùng để sắp xếp danh sách.
   * @return Chuỗi văn bản chứa thông tin danh sách.
   */
  private String buildSortedCustomerInfo(Comparator<Customer> comparator) {
    // Tạo một bản sao của danh sách để tránh làm thay đổi thứ tự danh sách gốc
    List<Customer> sortedList = new ArrayList<>(customerList);
    sortedList.sort(comparator);

    // Sửa lỗi: Bắt buộc dùng StringBuilder để nối chuỗi thay vì += để tránh rò rỉ bộ nhớ
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sortedList.size(); i++) {
      sb.append(sortedList.get(i).getCustomerInfo());
      if (i < sortedList.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}