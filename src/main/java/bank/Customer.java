package main.java.bank;

import bank.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Customer đại diện cho một khách hàng.
 */
public class Customer {
  private long idNumber;
  private String fullName;
  private List<Account> accountList;

  /**
   * Constructor không tham số (phục vụ MyTest).
   */
  public Customer() {
    this(0L, "");
  }

  // Constructor có tham số.
  public Customer(long idNumber, String fullName) {
    this.idNumber = idNumber;
    this.fullName = fullName;
    this.accountList = new ArrayList<Account>();
  }

  public long getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(long idNumber) {
    this.idNumber = idNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public List<Account> getAccountList() {
    return accountList;
  }

  // Thay đổi tài khoản trong Account.
  public void setAccountList(List<Account> accountList) {
    if (accountList == null) {
      this.accountList = new ArrayList<Account>();
    } else {
      this.accountList = accountList;
    }
  }

  /**
   * Thêm tài khoản cho khách hàng.
   */
  public void addAccount(Account account) {
    if (account == null) {
      return;
    }
    if (!accountList.contains(account)) {
      accountList.add(account);
    }
  }

  /**
   * Xóa tài khoản khỏi khách hàng.
   */
  public void removeAccount(Account account) {
    if (account == null) {
      return;
    }
    accountList.remove(account);
  }

  /**
   * Thông tin khách hàng dạng text.
   */
  public String getCustomerInfo() {
    return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
  }
}
