package dataobjects;

public class BankUser {
  private int bankUserID;
  private String username;
  private String userPassword;
  private int userType;

  public BankUser () {
    this(0, "", "", 0);
  }

  public BankUser (
      int initialId,
      String initialUsername,
      String initialPassword,
      int initialType) {
    this.bankUserID = initialId;
    this.userPassword = initialPassword;
    this.username = initialUsername;
    this.userType = initialType;
  }

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
