package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAO {
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  private String sql;
  private final boolean PRODUCTION_VALUE = true;
  private final Connection conn =
      ConnectionFactory.getInstance().getConnection(PRODUCTION_VALUE);
  private PreparedStatement ps;
  
  public BankAccountDAO() {
    super();
    try {
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      // I don't really know why this would happen
      logger.fatal(e);
    }
  }

  /**
   * Create Bank Account Function
   * @param BankAccount account
   *  This is the account that needs to be inserted into the database
   * @return an integer representing the number of rows that have been inserted (should
   * be 1)
   */
  public int createBankAccount(BankAccount account) {
    sql = "INSERT INTO BANK_ACCOUNT (BALANCE, STATUS, ACCOUNTTYPE) VALUES (?, ?, ?)";

    try {
      ps = conn.prepareStatement(sql);
      ps.setDouble(1, account.getBalance());
      ps.setInt(2, account.getStatus());
      ps.setInt(3, account.getAccounttype());
      return ps.executeUpdate();
    } catch (SQLException e) {
      // I don't really know why this would happen
      logger.fatal(e);
      return 0;
    }
  }

  // Read
  public BankAccount readBankAccount(int bankAccountid) {
    sql = "SELECT * FROM BANK_ACCOUNT WHERE BANK_ACCOUNTID = ?";
    BankAccount returnAccount = new BankAccount();
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
      } else {
        logger.info("Did not get an account");
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
    StringBuilder sqlStringBuilder = new StringBuilder();
    sqlStringBuilder.append("UPDATE BANK_ACCOUNT");
    sqlStringBuilder.append("SET ACCOUNTTYPE = ?, BALANCE = ?, STATUS = ?");
    sqlStringBuilder.append("WHERE BANK_ACCOUNTID = ?");
    sql = sqlStringBuilder.toString();
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, updateAccount.getAccounttype());
      ps.setDouble(2, updateAccount.getBalance());
      ps.setDouble(3, updateAccount.getStatus());
      ps.setInt(4, updateAccount.getBankAccountid());
      return ps.executeUpdate();
    } catch (SQLException e) {
      // This is the kind of thing that is considered "bad"
      logger.fatal(e);
      return 0;
    }
  }

}
