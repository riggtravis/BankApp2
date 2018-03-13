package ui;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import daos.BankUserDAO;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class ConsoleUITest {
  final static Logger logger = Logger.getLogger(ConsoleUITest.class);
  final static ConsoleUI ui = new ConsoleUI();

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
    BankUser testUser = new BankUser(1, "usr", "psw", 1);
    Scanner sin = new Scanner("1 0");
    BankAccount accountReceived = ui.registerForAccount(testUser, sin);
    BankAccount expectedAccount = new BankAccount(1, 0.0, 0, 1);
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) accountReceived.getBalance());
    assertEquals(
        (Double) expectedAccount.getBalance(),
        (Double) new AccountDAO checkDAO.readBankAccount(1).getBalance());
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
