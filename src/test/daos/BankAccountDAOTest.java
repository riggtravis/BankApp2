package daos;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAOTest {
  private final boolean PRODUCTION_VALUE = false;
  private final Connection conn =
      ConnectionFactory.getInstance().getConnection(PRODUCTION_VALUE);
  private Savepoint s;
  private BankAccount testAccount;
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  private BankAccountDAO dao;
  
  @Before
  public void setUp() {
    String sql = "DELETE FROM BANK_ACCOUNT";
    StringBuilder sqlBuilder = new StringBuilder();
    sqlBuilder.append("INSERT INTO BANK_ACCOUNT");
    sqlBuilder.append("(BANK_ACCOUNTID, BALANCE, STATUS, ACCOUNTTYPE)");
    sqlBuilder.append("VALUES (?, ?, ?, ?)");
    testAccount = new BankAccount(2, 2.0, 2, 2);
    try {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.executeUpdate();
      sql = sqlBuilder.toString();
      ps = conn.prepareStatement(sql);
      ps.setInt(1, 1);
      ps.setDouble(2, 1.0D);
      ps.setInt(3, 1);
      ps.setInt(4, 1);
      ps.executeUpdate();
      s = conn.setSavepoint("testSavepoint");
    } catch (SQLException e) {
      // An exception during the set up for unit tests is considered "bad"
      logger.fatal(e);
    }
  }

  @After
  public void tearDown() {
    try {
      conn.rollback(s);
    } catch (SQLException e) {
      // This should also be put into the category of "bad"
      logger.fatal(e);
    }
  }
  
  @Test
  public void testReadAccount() {
    assertEquals(1, new BankAccountDAO().readBankAccount(1).getAccounttype());
  }

  /**
   * This test currently only passes on an entirely new database. The possible solutions to
   * this problem that I have come up with include the following:
   *  Using the before class method(s) to create a completely clean database
   *      PRO: Gauruntees that this test will be run correctly
   *      CON: Clears out any data that we might have actually been using
   *  Creating a special database for the tests to interact with and cleaning it out in the
   *    before class methods
   *      PRO: Gauruntees that this test will be run correctly
   *      CON: Setting up databases is *hard*
   *  Using other functions that will be created later on to back test this functionality
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
    logger.info("Inserted " + dao.createBankAccount(testAccount) + " acccounts");
    BankAccount checkAccount = dao.readBankAccount(1);
    assertEquals((Double) testAccount.getBalance(), (Double) checkAccount.getBalance());
  }
  
  @Test
  public void testUpdateBankAccountAndReadBankAccount() {
    dao = new BankAccountDAO();
    dao.updateBankAccount(new BankAccount(1, 2.0D, 2, 2));
    assertEquals((Double) 2.0D, (Double) dao.readBankAccount(1).getBalance());
  }

}
