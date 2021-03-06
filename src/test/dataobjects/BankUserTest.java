/**

import com.sun.corba.se.spi.orbutil.fsm.TestAction1;
import org.junit.Test;
import static org.junit.Assert.*;
 * These are the tests to ensure the correctness of the BankUser class
 */
package dataobjects;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Travis Rigg
 *
 */
public class BankUserTest {
  private BankUser testUser;

  @Before
  public void setUp () {
    testUser = new BankUser(0, "usr", "psw", 0);
  }

  @Test
  public void testGetBankUserID () {
    assertEquals((Integer) 0, (Integer) testUser.getBankUserID());
  }

  @Test
  public void testSetBankUserID () {
    testUser.setBankUserID(1);
    assertEquals(1, testUser.getBankUserID());
  }

  @Test
  public void testGetUsername () {
    assertEquals("usr", testUser.getUsername());
  }

  @Test
  public void testSetUsername () {
    testUser.setUsername("newUsername");
    assertEquals("newUsername", testUser.getUsername());
  }

  @Test
  public void testGetPassword () {
    assertEquals("psw", testUser.getPassword());
  }

  @Test
  public void testSetPassword () {
    testUser.setPassword("newPassword");
    assertEquals("newPassword", testUser.getPassword());
  }

  @Test
  public void testGetUserType () {
    assertEquals(0, testUser.getUserType());
  }

  @Test
  public void testSetUserType () {
    testUser.setUserType(1);
    assertEquals(1, testUser.getUserType());
  }

}
