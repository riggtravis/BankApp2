package ui;

import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import daos.BankUserDAO;
import dataobjects.BankUser;

public class ConsoleUI {
  private static final Logger logger = Logger.getLogger(ConsoleUI.class);
  
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
    BankUserDAO uDAO = new BankUserDAO();
    try {
      uDAO.createBankUser(returnUser);
      
      // Search the database by username
      return uDAO.readBankUserByUserName(returnUser.getUsername());
    } catch (SQLException e) {
      logger.error("SQL exception when registering");
      return returnUser;
    }
  }

}
