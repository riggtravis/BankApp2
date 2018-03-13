package ui;

import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import daos.BankUserDAO;
import dataobjects.BankUser;

public class ConsoleUI {
  private static final Logger logger = Logger.getLogger(ConsoleUI.class);
  private final BankUserDAO uDAO = new BankUserDAO();

  // Create a login
  public BankUser register(Scanner sin) {
    // Use a scanner to get the preferred username
    BankUser returnUser = new BankUser();
    System.out.println("You need a username. Enter one:");
    returnUser.setUsername(sin.next());
    System.out.println("That certainly is a username!");
    System.out.println("You also need a password. Enter one:");
    returnUser.setPassword(sin.next());

    // Try to insert the return user into the database
    try {
      uDAO.createBankUser(returnUser);

      // Search the database by username
      return uDAO.readBankUserByUserName(returnUser.getUsername());
    } catch (SQLException e) {
      logger.error("SQL exception when registering");
      return new BankUser();
    }
  }

  // Let an existing user login.
  public BankUser login(Scanner sin) {
    BankUser returnUser = new BankUser();
    System.out.println("What is your username? Enter now:");
    returnUser.setUsername(sin.next());
    try {
      returnUser = uDAO.readBankUserByUserName(returnUser.getUsername());
    } catch (SQLException e) {
      logger.fatal(e);
      System.out.println("Sorry. Something went wrong it seems");
    }
    System.out.println("Okay... But what's your password?");
    if (returnUser.getPassword().equals(sin.next())) {
      return returnUser;
    }
    return new BankUser();
  }

}
