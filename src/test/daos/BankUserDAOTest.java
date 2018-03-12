package daos;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class BankUserDAOTest {
  private BankUserDAO dao;
  final static Logger logger = Logger.getLogger(BankUserDAOTest.class);
  
  @Before
  public void setUp() {
    Connection conn = ConnectionFactory.getInstance().getConnection();
    try {
      PreparedStatement ps = conn.prepareStatement(
          "DELETE FROM BANK_USER WHERE BANK_USERID = 2");
      ps.executeQuery();
    } catch (SQLException e) {
      logger.fatal(e);
    }
    dao = new BankUserDAO();
  }
  
  @Test
  public void testRead() {
    assertEquals(1, dao.readBankUser(1).getUserType());
  }

  @Test
  public void testCreateBankUser() {
    BankUser testUser = new BankUser(2, "usr", "psw", 2);
    dao.createBankUser(testUser);
    assertEquals("usr", dao.readBankUser(2).getUsername());
  }
  
  @Test
  public void testUpdateBankUser() {
    BankUser testUser = new BankUser(3, "newUsername", "newPassword", 0);
    dao.updateBankUser(testUser);
    assertEquals("newUsername", dao.readBankUser(3).getUsername());
  }
  
  @Test
  public void testDelete() {
    dao.createBankUser(new BankUser(4, "deleteMe", "password", 4));
    dao.deleteUser(4);
    assertEquals(0, dao.readBankUser(4).getBankUserID());
  }

}
