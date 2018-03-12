package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class BankUserDAO {
  final static Logger logger = Logger.getLogger(BankUserDAO.class);
  private String sql;
  private Connection conn;
  private PreparedStatement ps;

  /**
   * Create
   */
  public int createBankUser(BankUser user) {
    sql = "INSERT INTO BANK_USER"
        + " (BANK_USERID, USERNAME, USER_PASSWORD, USER_TYPE)"
        + " VALUES (?, ?, ?, ?)";
    conn = ConnectionFactory.getInstance().getConnection();
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, user.getBankUserID());
      ps.setString(2, user.getUsername());
      ps.setString(3, user.getPassword());
      ps.setInt(4, user.getUserType());
      return ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
      return 0;
    }
  }

  /**
   * Read
   */
  public BankUser readBankUser(int userID) {
    BankUser returnUser = new BankUser();
    
    conn = ConnectionFactory.getInstance().getConnection();
    sql = "SELECT * FROM BANK_USER WHERE BANK_USERID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        logger.info("Got a user");
        returnUser.setBankUserID(rs.getInt("bank_userID"));
        returnUser.setPassword(rs.getString("user_password"));
        returnUser.setUsername(rs.getString("username"));
        returnUser.setUserType(rs.getInt("user_type"));
      } else {
        logger.info("Found no records matching " + userID);
      }
    } catch (SQLException e) {
      logger.fatal(e);
    }
    return returnUser;
  }

  /**
   * Update
   */
  public int updateBankUser(BankUser updateUser) {
    conn = ConnectionFactory.getInstance().getConnection();
    sql = "UPDATE BANK_USER"
        + " SET USERNAME = ?, USER_PASSWORD = ?, USER_TYPE = ?"
        + " WHERE BANK_USERID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setString(1, updateUser.getUsername());
      ps.setString(2, updateUser.getPassword());
      ps.setInt(3, updateUser.getUserType());
      ps.setInt(4, updateUser.getBankUserID());
      return ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
      return 0;
    }
  }

  /**
   * Delete
   */
  public void deleteUser(int i) {
    conn = ConnectionFactory.getInstance().getConnection();
    sql = "DELETE FROM BANK_USERS WHERE BANK_ACCOUNTID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, i);
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }
}
