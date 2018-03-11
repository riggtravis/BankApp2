package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAO {
  final static Logger logger = Logger.getLogger(BankAccountDAO.class);
  private String sql;
  private Connection conn;
  private PreparedStatement ps;
  
  /**
   * Create Bank Account Function
   * @param BankAccount account
   *  This is the account that needs to be inserted into the database
   * @return an integer representing the number of rows that have been inserted (should
   * be 1)
   */
  public int createBankAccount(BankAccount account) {
    sql = "INSERT INTO BANK_ACCOUNT"
        + " (BANK_ACCOUNTID, BALANCE, STATUS, ACCOUNTTYPE) "
        + " VALUES (?, ?, ?, ?)";
    conn = ConnectionFactory.getInstance().getConnection();

    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, account.getBankAccountid());
      ps.setDouble(2, account.getBalance());
      ps.setInt(3, account.getStatus());
      ps.setInt(4, account.getAccounttype());
      return ps.executeUpdate();
    } catch (SQLException e) {
      // I don't really know why this would happen
      logger.fatal(e);
      return 0;
    }
  }

  // Read
  public BankAccount readBankAccount(int bankAccountid) {
    BankAccount returnAccount = new BankAccount();

    conn = ConnectionFactory.getInstance().getConnection();
    sql = "SELECT * FROM BANK_ACCOUNT WHERE BANK_ACCOUNTID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, bankAccountid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        logger.info("Got an account");
        returnAccount.setAccounttype(rs.getInt("accountType"));
        returnAccount.setBalance(rs.getDouble("balance"));
        returnAccount.setBankAccountid(rs.getInt("bank_accountID"));
        returnAccount.setStatus(rs.getInt("status"));
        logger.info("Got info " + returnAccount.getAccounttype());
      } else {
        logger.info("Found no records matching " + bankAccountid);
      }
    } catch (SQLException e) {
      // This happening seems bad. I don't know what causes it.
      logger.fatal(e);
    }
    return returnAccount;
  }
  
  /**
   * Update function
   * @param an account (with a valid id) to be updated
   * @return the number of rows that were updated
   */
  public int updateBankAccount(BankAccount updateAccount) {
    conn = ConnectionFactory.getInstance().getConnection();
    sql = "UPDATE BANK_ACCOUNT"
        + " SET ACCOUNTTYPE = ?, BALANCE = ?, STATUS = ?"
        + " WHERE BANK_ACCOUNTID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, updateAccount.getAccounttype());
      ps.setDouble(2, updateAccount.getBalance());
      ps.setInt(3, updateAccount.getStatus());
      ps.setInt(4, updateAccount.getBankAccountid());
      ps.setQueryTimeout(32);
      logger.info("About to update a record");
      return ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
      return 0;
    }
  }

  public void deleteAccount(int i) {
    conn = ConnectionFactory.getInstance().getConnection();
    sql = "DELETE FROM BANK_ACCOUNT WHERE BANK_ACCOUNTID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, i);
      ps.setQueryTimeout(32);
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

}
