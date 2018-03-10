package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAO {
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  
  // Create
  public void createBankAccount(BankAccount account) {
    String sql = "INSERT INTO ACCOUNT (BALANCE, STATUS, ACCOUNTTYPE) VALUES (?, ?, ?)";
    
    Connection conn = ConnectionFactory.getInstance().getConnection();
    
    try {
      conn.setAutoCommit(false);
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setDouble(1, account.getBalance());
      ps.setInt(2, account.getStatus());
      ps.setInt(3, account.getAccounttype());
    } catch (SQLException e) {
      // I don't really know why this would happen
      logger.fatal(e);
    }
  }
  
  // Read
  public BankAccount readBankAccount() {
    return new BankAccount();
  }

}
