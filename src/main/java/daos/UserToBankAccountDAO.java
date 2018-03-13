package daos;

import dataobjects.BankAccount;
import dataobjects.BankUser;

public class UserToBankAccountDAO {
  
  public void createUserToBankAccountRelationship (
      BankUser owner, BankAccount account) {
    BankAccountDAO accountDAO = new BankAccountDAO();
    sql = "INSERT INTO "
  }

}
