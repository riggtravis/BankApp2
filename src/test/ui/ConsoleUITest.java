package ui;

import com.sun.corba.se.spi.orbutil.fsm.TestAction1;
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
    testUser = new BankUser(1, "usr", "psw", 1);
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

  @Test
  public void testApply() {
    sin = new Scanner("1 0");
    BankAccount accountReceived = ui.registerForAccount(testUser, sin);
    BankAccount expectedAccount = new BankAccount(1, 0.0D, 0, 1);
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) accountReceived.getBalance());
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) new BankAccountDAO().readBankAccount(1).getBalance());
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
    assertEquals((Integer) 4, ui.peruseAccounts(testUser, new Scanner("3"));
  }

  @After
  public void tearDown() {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_USER WHERE USERNAME = 'GreatUsername'");
      ps.executeQuery();
      ps = conn.prepareStatement("DELETE FROM BANK_ACCOUNT");
      ps.executeQuery();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

}
