package daos;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import dataobjects.BankUser;
import utlities.ConnectionFactory;

public class BankUserDAOTest {
  private BankUserDAO dao;
  final static Logger logger = Logger.getLogger(BankUserDAOTest.class);
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
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
    try {
      assertEquals(1, dao.readBankUser(1).getUserType());
    } catch (SQLException e) {
      fail("SQLException");
    }
  }
  
  @Test
  public void testUsernameRead() {
    try {
      assertEquals(1, dao.readBankUserByUserName("usr").getBankUserID());
    } catch (SQLException e) {
      fail("there was an SQL error when testing the username read");
    }
  }

  @Test
  public void testCreateBankUser() {
    BankUser testUser = new BankUser(2, "NewUsr", "psw", 2);
    try {
      dao.createBankUser(testUser);
    } catch (SQLException e) {
      fail("SQLException");
    }
    try {
      assertEquals("NewUsr", dao.readBankUser(2).getUsername());
    } catch (SQLException e) {
      fail("SQLException");
    }
  }
  
  @Test(expected = SQLException.class)
  public void testCreateBankUserBad() throws SQLException {
    dao.createBankUser(new BankUser(3, "usr", "psw", 1));
  }
  
  @Test
  public void testUpdateBankUser() {
    BankUser testUser = new BankUser(3, "newUsername", "newPassword", 0);
    dao.updateBankUser(testUser);
    try {
      assertEquals("newUsername", dao.readBankUser(3).getUsername());
    } catch (SQLException e) {
      fail("SQLException");
    }
  }
  
  @Test
  public void testDelete() {
    try {
      dao.createBankUser(new BankUser(4, "deleteMe", "password", 4));
    } catch (SQLException e) {
      fail("SQLException");
    }
    dao.deleteUser(4);
    try {
      assertEquals(0, dao.readBankUser(4).getBankUserID());
    } catch (SQLException e) {
      fail("SQLException");
    }
  }

}
