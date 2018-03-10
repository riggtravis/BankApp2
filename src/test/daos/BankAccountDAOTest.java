package daos;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataobjects.BankAccount;
import utlities.ConnectionFactory;

public class BankAccountDAOTest {
  private final Connection conn = ConnectionFactory.getInstance().getConnection();
  private Savepoint s;
  private BankAccount testAccount;
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);

  @Before
  public void setUp() {
    testAccount = new BankAccount(1, 1.0, 1, 1);
    try {
      s = conn.setSavepoint("testSavepoint");
    } catch (SQLException e) {
      // An exception during the set up for unit tests I think should be considered "bad"
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
  public void testCreateBankAccountAndReadBankAccount() {
    BankAccountDAO dao = new BankAccountDAO();
    dao.createBankAccount(testAccount);
    BankAccount checkAccount = dao.readBankAccount(1);
    assertEquals((Double) testAccount.getBalance(), (Double) checkAccount.getBalance());
  }

}
