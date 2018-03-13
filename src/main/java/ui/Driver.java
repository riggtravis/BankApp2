package ui;

import java.util.Scanner;

import dataobjects.BankAccount;
import dataobjects.BankUser;

public class Driver {

  public static void main (String args[]) {
    ConsoleUI ui = new ConsoleUI();
    Scanner sin = new Scanner(System.in);
    BankUser mainUser;
    BankAccount mainAccount;
    System.out.println("1 for login 2 for register");
    switch(sin.next()) {
    case "1":
      mainUser = ui.login(sin);
      if (mainUser.getUsername().equals("")) {
        System.out.println("Sorry, you're not registered");
        return;
      }
      break;
    case "2":
      mainUser = ui.register(sin);
      break;
    default:
      System.out.println("option not recognized");
      System.out.println("Goodbye");
      return;
    }
    
    // Let the user interact with an account
    System.out.println("1 to register for a new account");
    System.out.println("2 to interact with an existing account");
    switch(sin.next()) {
    case "1":
      mainAccount = ui.registerForAccount(mainUser, sin);
      break;
    case "2":
      mainAccount = ui.peruseAccounts(mainUser, sin);
      break;
    default:
      System.out.println("option not recognized");
      System.out.println("Goodbye");
      return;
    }
    
    ui.interactWithBankAccount(mainUser, mainAccount, sin);
    System.out.println("Thank you for choosing Billions Bank! We promise not to steal your money!");
  }
}
