package ui;

import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import daos.BankUserDAO;
import dataobjects.BankAccount;
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

  // Let a logged in user apply for an account
  public BankAccount registerForAccount(BankUser currentUser, Scanner sin) {
    int accountType;
    double initialDeposit;
    BankAccount returnAccount = new BankAccount();

    System.out.println("Please enter the type of account you would like:");
    System.out.println("1. Checking");
    System.out.println("2. Savings");
    System.out.println("3. Money Market Account");
    System.out.println("4. Certificate of Deposit");
    System.out.println("5. Investment Retirement Account");
    System.out.println("6. Brokerage");
    returnAccount.setAccounttype(sin.nextInt());

    System.out.println("Please enter an initial deposit");
    returnAccount.setBalance(sin.nextDouble());

    // Associate the user and their new account
  }

}
