package dataobjects;

public class BankUser {
  private int bankUserID;
  private String username;
  private String userPassword;
  private int userType;

  public int getBankUserID () {
    return this.bankUserID;
  }

  public void setBankUserID (int newUserID) {
    this.bankUserID = newUserID;
  }

  public String getUsername () {
    return this.username;
  }

  public void setUsername (String newUsername) {
    this.username = newUsername;
  }

  public String getPassword () {
    return this.userPassword;
  }

  public void setPassword (String newPassword) {
    this.userPassword = newPassword;
  }

  public int getUserType () {
    return this.userType;
  }

  public void setUserType (int newType) {
    this.userType = newType;
  }
}
