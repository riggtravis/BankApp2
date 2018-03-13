package daos;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserToBankAccountDAOTest {

  @Test
  public void testCreateUserToBankAccountRelationship() {
    UserToBankAccountDAO testDAO = new UserToBankAccountDAO();
    BankUserDAO userDAO = new BankUserDAO();
    BankAccountDAO accountDAO = new BankAccountDAO();
    Connection conn = ConnectionFactory.getInstance().getConnection();

    // Get a user to be the owner of the new account
    BankUser testUser = userDAO.readBankUserByUserName("usr");
    testDAO.createUserToBankAccountRelationship(
        testUser, new BankAccount(0, 112.0D, 0, 1));

    /* Make sure that the newest bank account and the bank account that was just
     * inserted share the same VALUES
     */
    // Get the largest bankAccountid we can find
    CallableStatement number getter =
        conn.prepareCall("{? = CALL HIGHEST_BANK_ACCOUNT_ID}");
    numberGetter.registerOutParameter(1, Types.INTEGER);
    numberGetter.executeUpdate();

    assertEquals(
        (Double) 112.0D, accountDAO.readBankAccount(numberGetter.getInt(1)));
  }

}
