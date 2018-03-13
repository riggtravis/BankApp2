package daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import org.apache.log4j.Logger;

import dataobjects.BadTransactionException;
import dataobjects.BankAccount;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class UserToBankAccountDAO {
  final static Logger logger = Logger.getLogger(UserToBankAccountDAO.class);
  private String sql;
  private Connection conn;
  PreparedStatement ps;

  public BankAccount createUserToBankAccountRelationship (
        BankUser owner, BankAccount account) throws SQLException {
    conn = ConnectionFactory.getInstance().getConnection();
    BankAccountDAO accountDAO = new BankAccountDAO();
    sql = "INSERT INTO USER_TO_BANK_ACCOUNT"
        + " (BANK_USERID, BANK_ACCOUNTID) VALUES (?, ?)";
    int finalID;
    ps = conn.prepareStatement(sql);
    logger.info("Prepared statement okay");
    ps.setInt(1, owner.getBankUserID());
    logger.info("Successfully prepared the query for the user");

    /* Now we need to insert the bank account and get the ID that the database
     * assigns to it
     */
    accountDAO.createBankAccount(account);

    // We should be able to get the most recent entry with the highest ID
    CallableStatement numberGetter =
        conn.prepareCall("{? = call HIGHEST_BANK_ACCOUNT_ID()}");
    logger.info("Successfully prepared for function call");
    numberGetter.registerOutParameter(1, Types.INTEGER);
    logger.info("Successfully loaded the function call");
    numberGetter.executeUpdate();
    logger.info("Successfully called the function call");

    // Now we need to insert a relational entity into the database
    finalID = numberGetter.getInt(1);
    logger.info("Successfully retrived a number");
    ps.setInt(2, finalID);
    logger.info("Succeeded in setting up the query for the account");
    ps.executeUpdate();
    logger.info("Succeeded in updating the table");

    // Now return the bank account that was created
    return accountDAO.readBankAccount(finalID);
  }

  public Vector<BankAccount> readBankUserAccounts(BankUser requestUser) {
    Vector<BankAccount> returnVector = new Vector<BankAccount>();
    ResultSet rs;
    conn = ConnectionFactory.getInstance().getConnection();

    // Get all of the accounts from the account table that belong to requestUser
    sql = "SELECT AC.*"
        + " FROM USER_TO_BANK_ACCOUNT U JOIN BANK_ACCOUNT AC"
        + " ON AC.BANK_ACCOUNTID = U.BANK_ACCOUNTID WHERE U.BANK_USERID = ?";
    try {
      ps = conn.prepareStatement(sql);
      ps.setInt(1, requestUser.getBankUserID());
      rs = ps.executeQuery();
      while (rs.next()) {
        BankAccount listedAccount = new BankAccount();
        listedAccount.setAccounttype(rs.getInt("accountType"));
        listedAccount.setBalance(rs.getDouble("balance"));
        listedAccount.setBankAccountid(rs.getInt("bank_accountID"));
        listedAccount.setStatus(rs.getInt("status"));
        returnVector.add(listedAccount);
      }
    } catch (SQLException e) {
      logger.error(e);
    } catch (BadTransactionException e) {
      logger.error("Bad data was allowed in the database");
    }

    return returnVector;
  }
  
}
