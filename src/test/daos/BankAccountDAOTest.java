package daos;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAOTest {
  private Connection conn;
  private BankAccount testAccount;
  final static Logger logger = Logger.getLogger(BankAccountDAOTest.class);
  private BankAccountDAO dao;
  
  @Before
  public void setUp() {
    conn = ConnectionFactory.getInstance().getConnection();
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_ACCOUNT WHERE BANK_ACCOUNTID = 2)");
      ps.executeQuery();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

  @Test
  public void testReadAccount() {
    assertEquals(1, new BankAccountDAO().readBankAccount(1).getAccounttype());
  }

  /**
   * This test currently only passes on an entirely new database. The possible
   * solutions to this problem that I have come up with include the following:
   *  Using the before class method(s) to create a completely clean database
   *      PRO: Gauruntees that this test will be run correctly
   *      CON: Clears out any data that we might have actually been using
   *  Creating a special database for the tests to interact with and cleaning
   *  it out in the before class methods
   *      PRO: Gauruntees that this test will be run correctly
   *      CON: Setting up databases is *hard*
   *  Using other functions that will be created later on to back test this
   *  functionality
   *      PRO: Requires no immediate action
   *        Will work (eventually)
   *      CON: Is not unit testing
   *  Creating a test entry in our database to run tests on
   *      PRO: Technically works
   *      CON: Gross
   *        Seems bad
   *        Probably gonna do it anyway because I'm a bad human being
   *  Just leaving everything as is
   *      PRO: Requires no immediate or long term action
   *      CON: Is annoying
   */
  @Test
  public void testCreateBankAccountAndReadBankAccount() {
    dao = new BankAccountDAO();
    testAccount = new BankAccount(2, 2.0D, 2, 2);
    logger.info(
        "Inserted " + dao.createBankAccount(testAccount) + " acccounts");
    BankAccount checkAccount = dao.readBankAccount(2);
    assertEquals(
        (Double) testAccount.getBalance(), (Double) checkAccount.getBalance());
  }
  
  @Test
  public void testUpdateBankAccountAndReadBankAccount() {
    dao = new BankAccountDAO();
    dao.updateBankAccount(new BankAccount(3, 2.0D, 2, 2));
    assertEquals((Double) 2.0D, (Double) dao.readBankAccount(3).getBalance());
  }
  
  @Test
  public void testCreateAndDelete() {
    dao = new BankAccountDAO();
    dao.createBankAccount(new BankAccount(4, 4.0D, 4, 4));
    dao.deleteAccount(4);
    assertEquals(0, dao.readBankAccount(4).getBankAccountid());
  }

}
