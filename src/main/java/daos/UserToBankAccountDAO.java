package daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import dataobjects.BankAccount;
import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class UserToBankAccountDAO {

  public BankAccount createUserToBankAccountRelationship (
        BankUser owner, BankAccount account) throws SQLException {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    PreparedStatement ps;
    BankAccountDAO accountDAO = new BankAccountDAO();
    String sql =
      "INSERT INTO USER_TO_BANK_ACCOUNT (USERID, BANK_ACCOUNTID) VALUES (?, ?)";
    int finalID;
    ps = conn.prepareStatement(sql);
    ps.setInt(1, owner.getBankUserID());

    /* Now we need to insert the bank account and get the ID that the database
     * assigns to it
     */
    accountDAO.createBankAccount(account);

    // We should be able to get the most recent entry with the highest ID
    CallableStatement numberGetter =
        conn.prepareCall("{? = CALL HIGHEST_BANK_ACCOUNT_ID}");
    numberGetter.registerOutParameter(1, Types.INTEGER);
    numberGetter.executeUpdate();

    // Now we need to insert a relational entity into the database
    finalID = numberGetter.getInt(1);
    ps.setInt(2, numberGetter.getInt(1));
    ps.executeUpdate();
    
    // Now return the bank account that was created
    return accountDAO.readBankAccount(finalID);
  }

}
