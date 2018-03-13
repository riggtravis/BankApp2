package daos;

import static org.junit.Assert.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import dataobjects.BankAccount;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class UserToBankAccountDAOTest {
  final static Logger logger = Logger.getLogger(UserToBankAccountDAOTest.class);
  final static Connection conn =
      ConnectionFactory.getInstance().getConnection();
  UserToBankAccountDAO testDAO = new UserToBankAccountDAO();

  @Test
  public void testCreateUserToBankAccountRelationship() {
    BankUserDAO userDAO = new BankUserDAO();
    BankAccountDAO accountDAO = new BankAccountDAO();

    // Get a user to be the owner of the new account
    BankUser testUser;
    try {
      testUser = userDAO.readBankUserByUserName("usr");
      logger.info("Got user!");
      testDAO.createUserToBankAccountRelationship(
          testUser, new BankAccount(0, 112.0D, 0, 1));
      logger.info("Inserted data!");
      /* Make sure that the newest bank account and the bank account that was
       * just inserted share the same VALUES
       */
      // Get the largest bankAccountid we can find
      CallableStatement numberGetter = conn.prepareCall(
          "{? = call HIGHEST_BANK_ACCOUNT_ID()}");
      logger.info("Getting ready to get the highest number!");
      numberGetter.registerOutParameter(1, Types.INTEGER);
      logger.info("Very ready to get the biggest number!");
      numberGetter.executeUpdate();
      logger.info("Got the biggest number!");
      assertEquals(
          (Double) 112.0D,
          (Double) accountDAO.readBankAccount(
              numberGetter.getInt(1)).getBalance());
    } catch (SQLException e) {
      fail("SQL errors abound!");
    }
  }

  public void testReadBankUserAccounts() {
    Vector<BankAccount> fetchedVector =
        testDAO.readBankUserAccounts(new BankUser(1, "usr", "psw", 1));
    assertEquals((Integer) 3, fetchedVector.size());
  }

  @After
  public void tearDown() {
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_ACCOUNT WHERE BANK_ACCOUNTID = ?");
      CallableStatement numberGetter = conn.prepareCall(
          "{? = call HIGHEST_BANK_ACCOUNT_ID()}");
      numberGetter.registerOutParameter(1, Types.INTEGER);
      numberGetter.executeUpdate();
      ps.setInt(1, numberGetter.getInt(1));
      ps.executeUpdate();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

}
