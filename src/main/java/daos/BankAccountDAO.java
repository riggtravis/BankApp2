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
  private Connection conn = ConnectionFactory.getInstance().getConnection();

  /**
   * Create Bank Account Function
   * @param BankAccount account
   *  This is the account that needs to be inserted into the database
   * @return an integer representing the number of rows that have been inserted (should
   * be 1)
   */
  public int createBankAccount(BankAccount account) {
    sql = "INSERT INTO ACCOUNT (BALANCE, STATUS, ACCOUNTTYPE) VALUES (?, ?, ?)";

    try {
      conn.setAutoCommit(false);
      PreparedStatement ps = conn.prepareStatement(sql);
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
    sql = "SELECT * FROM ACCOUNT WHERE BANK_ACCOUNT = ?";
    BankAccount returnAccount = new BankAccount();
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, bankAccountid);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        logger.info("Got an account");
        returnAccount.setAccounttype(rs.getInt("accountType"));
        returnAccount.setBalance(rs.getDouble("balance"));
        returnAccount.setBankAccountid(rs.getInt("bankAccountID"));
        returnAccount.setStatus(rs.getInt("status"));
      }
    } catch (SQLException e) {
      // This happening seems bad. I don't know what causes it.
      logger.fatal(e);
    }
    return returnAccount;
  }

}
