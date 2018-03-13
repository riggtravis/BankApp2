package ui;

import daos.BankAccountDAO;
import daos.BankUserDAO;
import dataobjects.BankAccount;
import dataobjects.BankUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import utlities.ConnectionFactory;

public class ConsoleUITest {
  final static Logger logger = Logger.getLogger(ConsoleUITest.class);
  final static ConsoleUI ui = new ConsoleUI();
  private BankUser testUser;
  private Scanner sin;

  @Before
  public void setUp() {
    testUser = new BankUser(3, "newUsername", "newPassword", 0);
  }

  @Test
  public void testRegister() {
    BankUserDAO dao = new BankUserDAO();

    // We need to pass register a scanner
    Scanner sin = new Scanner("GreatUsername GreatPassword");
    BankUser got = ui.register(sin);
    try {
      assertEquals(
          got.getBankUserID(),
          dao.readBankUserByUserName("GreatUsername").getBankUserID());
    } catch (SQLException e) {
      fail("You didn't even get SQL right");
    }
  }

  @Test
  public void badRegister() throws SQLException {
    BankUserDAO dao = new BankUserDAO();

    Scanner sin = new Scanner("usr psw");
    BankUser returnedUser = ui.register(sin);
    assertEquals("", returnedUser.getUsername());
  }

  @Test
  public void testLogin() {
    BankUserDAO dao = new BankUserDAO();

    Scanner sin = new Scanner("usr psw");
    BankUser returnedUser = ui.login(sin);
    try {
      assertEquals(
          dao.readBankUserByUserName("usr").getBankUserID(),
          returnedUser.getBankUserID());
    } catch (SQLException e) {
      fail("Something went wrong with your SQL there, bud");
    }
  }

  @Test
  public void testBadLogin() {
    BankUserDAO dao = new BankUserDAO();
    Scanner sin = new Scanner("usr blurble");
    BankUser returnedUser = ui.login(sin);
    assertEquals("", returnedUser.getUsername());
  }

  // This test requires a clean database. I need to figure out how better to provide that
  @Test
  public void testApply() {
    sin = new Scanner("1 0");
    BankAccount accountReceived = ui.registerForAccount(testUser, sin);
    logger.info("Got account " + accountReceived.getBankAccountid());
    BankAccount expectedAccount = new BankAccount(5, 0.0D, 0, 1);
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) accountReceived.getBalance());
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) new BankAccountDAO().readBankAccount(5).getBalance());
    
    // We need to delete the account we created
    BankAccountDAO dao = new BankAccountDAO();
    dao.deleteAccount(accountReceived.getBankAccountid());
  }

  // We need to make sure that bad accounts aren't successfully created
  @Test
  public void testBadApply() {
    sin = new Scanner("1 -1");
    BankAccount accountReceived = ui.registerForAccount(testUser, sin);
    assertEquals((Double) 0.0D, (Double) accountReceived.getBalance());
  }
  
  @Test
  public void testPeruseAccounts() {
    assertEquals((Integer) 8, (Integer) ui.peruseAccounts(testUser, new Scanner("2")).getBankAccountid());
  }


  @After
  public void tearDown() {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_USER WHERE USERNAME = 'GreatUsername'");
      ps.executeQuery();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

}
