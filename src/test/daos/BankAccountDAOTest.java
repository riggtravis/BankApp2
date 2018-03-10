package daos;

import static org.junit.Assert.*;

import org.junit.Test;

public class BankAccountDAOTest {
  private final conn = ConnectionFactory.getInstance.getConnection();
  private Savepoint s;
  private BankAccount testAccount;

  @Before
  public void setUp() {
    BankAccount = new BankAccount(1, 1.0, 1, 1);
    s = conn.setSavepoint("testSavepoint");
  }

  @After
  public void tearDown() {
    conn.rollback(s);
  }

  @Test
  public void testCreateBankAccountAndReadBankAccount() {
    BankAccountDAO dao = new BankAccountDAO();
    dao.createBankAccount(testAccount);
    BankAccountDAO checkAccount = dao.readBankAccount(1);
    assertEquals(testAccount, checkAccount);
  }

}
