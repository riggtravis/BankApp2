package daos;

import dataobjects.BankAccount;
import dataobjects.BankUser;

public class UserToBankAccountDAO {

  public void createUserToBankAccountRelationship (
        BankUser owner, BankAccount account) {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    PreparedStatement ps;
    BankAccountDAO accountDAO = new BankAccountDAO();
    String sql =
      "INSERT INTO USER_TO_BANK_ACCOUNT (USERID, BANK_ACCOUNTID) VALUES (?, ?)";
    ps = conn.preprepareStatement(sql);
    ps.setInt(1, owner.getBankUserID());

    /* Now we need to insert the bank account and get the ID that the database
     * assigns to it
     */
    accountDAO.createBankAccount(account);

    // We should be able to get the most recent entry with the highest ID
    CallableStatement numberGetter =
        conn.preprepareStatement("{? = CALL HIGHEST_BANK_ACCOUNT_ID}");
    numberGetter.registeregisterOutParameter(1, Types.INTEGER);
    numberGetter.executeUpdate();

    // Now we need to insert a relational entity into the database
    ps.setInt(2, numberGetter.getInt(1));
    ps.executeUpdate();
  }

}
