package dataobjects;

import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.*;

public class BankAccountTest {
	BankAccount testAccount;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void beforeEach () {
		testAccount = new BankAccount();
	}

	@Test
	public void testGetBankAccounid() {
		assertEquals(0, testAccount.getBankAccountid());
	}

	@Test
	public void testSetAccountid() {
		testAccount.setStatus(1);
		assertEquals(1, testAccount.getStatus());
	}

	@Test
	public void testGetBalance() {
		assertEquals((Double) 0.0D, (Double) testAccount.getBalance());
	}

	@Test
	public void testSetBalance() {
		try {
      testAccount.setBalance(0.1D);
    } catch (BadTransactionException e) {
      fail("Your setter is treating " + 0.1D + " as a negative number");
    }
		assertEquals((Double) 0.1D, (Double) testAccount.getBalance());
	}
	
	@Test(expected = BadTransactionException.class)
	public void testBadSet() throws BadTransactionException {
	  testAccount.setBalance(-0.1D);
	  thrown.expect(BadTransactionException.class);
	}

	@Test
	public void testGetStatus() {
		assertEquals(0, testAccount.getStatus());
	}

	@Test
	public void testSetStatus() {
		testAccount.setStatus(1);
		assertEquals(1, testAccount.getStatus());
	}

	@Test
	public void testGetAccounttype() {
		assertEquals(0, testAccount.getAccounttype());
	}

	@Test
	public void testSetAccounttype() {
		testAccount.setAccounttype(1);
		assertEquals(1, testAccount.getAccounttype());
	}

	@Test
	public void testMakeDeposit() {
		try {
			testAccount.makeDeposit(0.1D);
		} catch (BadTransactionException e) {
			fail("Deposit treated as bad");
		}
		assertEquals((Double) 0.1D, (Double) testAccount.getBalance());
	}
	
	@Test(expected = BadTransactionException.class)
	public void testBadDeposit() throws BadTransactionException {
		testAccount.makeDeposit(-0.1D);
		thrown.expect(BadTransactionException.class);
	}

	@Test
	public void testMakeWithdrawal() {
		try {
			testAccount.makeDeposit(1.0D);
      testAccount.makeWithdrawal(0.1D);
		} catch (BadTransactionException e) {
			fail("Either the deposit or the withdrawal was bad");
		}
		assertEquals((Double) 0.9D, (Double) testAccount.getBalance());
	}
	
	@Test(expected = BadTransactionException.class)
	public void testBadWithdrawal() throws BadTransactionException {
	  testAccount.makeDeposit(-0.1D);
		thrown.expect(BadTransactionException.class);
	}

}
