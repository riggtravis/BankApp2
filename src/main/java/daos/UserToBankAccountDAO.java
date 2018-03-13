package daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import dataobjects.BankAccount;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class UserToBankAccountDAO {
  final static Logger logger = Logger.getLogger(UserToBankAccountDAO.class);

  public BankAccount createUserToBankAccountRelationship (
        BankUser owner, BankAccount account) throws SQLException {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    PreparedStatement ps;
    BankAccountDAO accountDAO = new BankAccountDAO();
    String sql =
      "INSERT INTO USER_TO_BANK_ACCOUNT (BANK_USERID, BANK_ACCOUNTID) VALUES (?, ?)";
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

}
