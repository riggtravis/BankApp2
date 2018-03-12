package ui;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Scanner;

import org.junit.Test;

import daos.BankUserDAO;
import dataobjects.BankUser;

public class ConsoleUITest {

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

}
