package daos;

public class BankUserDAO {
  final static Logger logger = Logger.getLogger(BankUserDAO.class);
  private String sql;
  private Connection conn;

  /**
   * Create
   */
  public int createBankUser(BankUser user) {
    sql = "INSERT INTO BANK_USER"
        + " (BANK_USERID, USERNAME, USER_PASSWORD, USER_TYPE)"
        + " VALUES (?, ?, ?, ?)";
    conn = ConnectionFactory.getInstance().getConnection();
  }

  /**
   * Read
   */

  /**
   * Update
   */

  /**
   * Delete
   */
}
