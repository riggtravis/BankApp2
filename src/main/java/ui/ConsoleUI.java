package ui;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

import org.apache.log4j.Logger;

import daos.BankAccountDAO;
import daos.BankUserDAO;
import daos.UserToBankAccountDAO;
import dataobjects.BadTransactionException;
import dataobjects.BankAccount;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

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
    BankAccount returnAccount = new BankAccount();
    UserToBankAccountDAO relationalDAO = new UserToBankAccountDAO();

    System.out.println("Please enter the type of account you would like:");
    System.out.println("1. Checking");
    System.out.println("2. Savings");
    System.out.println("3. Money Market Account");
    System.out.println("4. Certificate of Deposit");
    System.out.println("5. Investment Retirement Account");
    System.out.println("6. Brokerage");
    returnAccount.setAccounttype(sin.nextInt());

    System.out.println("Please enter an initial deposit");
    try {
      returnAccount.setBalance(sin.nextDouble());
    } catch (BadTransactionException e) {
      System.out.println(
            "Why are you trying to make a negative initial deposit?");
      return new BankAccount();
    }

    // Associate the user and their new account
    try {
      return relationalDAO.createUserToBankAccountRelationship(
          currentUser, returnAccount);
    } catch (SQLException e) {
      System.out.println("Something was done that shouldn't have been.");
      return new BankAccount();
    }
  }

  // Let the user peruse all their accounts
  public BankAccount peruseAccounts(BankUser currentUser, Scanner sin) {
    UserToBankAccountDAO dao = new UserToBankAccountDAO();
    Vector<BankAccount> perusalVector = dao.readBankUserAccounts(currentUser);

    // Loop through the persual vector to show the user all of their choices
    System.out.println("Which of your accounts would you like to access?");
    for (int i = 0; i < perusalVector.size(); i++) {
      int menu = i + 1;
      System.out.println(
          menu + ". " + "Account "
          + perusalVector.elementAt(i).getBankAccountid() + ": $"
          + perusalVector.elementAt(i).getBalance());
    }

    // Subtract one to adjust from natural numbers to computer natural numbers
    return perusalVector.elementAt(sin.nextInt() - 1);
  }
  
  // Let the user interact with one of their accounts
  public void interactWithBankAccount(BankUser currentUser, BankAccount userAccount, Scanner sin) {
    BankAccountDAO aDAO = new BankAccountDAO();
    System.out.println("What action would you like to perform with your account?");
    System.out.println("1. Deposit");
    System.out.println("2. Withdraw");
    System.out.println("3. Transfar");
    switch (sin.next()) {
    case "1":
      // Find out how much the user wants to deposit
      System.out.println("How much would you like to deposit?");
      try {
        userAccount.makeDeposit(sin.nextDouble());
        aDAO.updateBankAccount(userAccount);
      } catch (BadTransactionException e) {
        System.out.println("Nice try, but that's not okay");
      }
      break;
    case "2":
      System.out.println("How much would you like to withdraw");
      try {
        userAccount.makeWithdrawal(sin.nextDouble());
        aDAO.updateBankAccount(userAccount);
      } catch (BadTransactionException e) {
        System.out.println("I mean... If you want to receive negative money...");
      }
      break;
    case "3":
      Connection conn = ConnectionFactory.getInstance().getConnection();
      try {
        CallableStatement transferHandler = conn.prepareCall("{call MAKE_TRANSFER(?, ?, ?)}");
        transferHandler.setInt(1, userAccount.getBankAccountid());
        System.out.println("Please enter the ID of the account you want to transfer to");
        transferHandler.setInt(2, sin.nextInt());
        System.out.println("Finally enter the ammount of money you would like to transfer");
        transferHandler.setDouble(3, sin.nextDouble());
        transferHandler.executeUpdate();
      } catch (SQLException e) {
        System.out.println("Sorry. Transfer wasn't successful");
      }
    default:
      break;
    }
  }

}
