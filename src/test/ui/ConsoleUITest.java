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

  @Test
  public void testRegister() {
    ConsoleUI ui = new ConsoleUI();
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
  
  @After
  public void tearDown() {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_USER WHERE USERNAME = 'GreatUser'");
      ps.executeQuery();
    } catch (SQLException e) {
      logger.fatal(e);
    }
  }

}
