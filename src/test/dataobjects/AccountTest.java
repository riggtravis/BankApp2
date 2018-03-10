package dataobjects;

import org.junit.rules.ExpectedException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.*;

public class AccountTest {
	Account testAccount;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void beforeEach () {
		testAccount = new Account();
	}

	@Test
	public void testGetAccounid() {
		assertEquals(0, testAccount.getAccountid());
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
		testAccount.setBalance(0.1D);
		assertEquals((Double) 0.1D, (Double) testAccount.getBalance());
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
		} catch (BadDepositException e) {
			fail("Deposit treated as bad");
		}
		assertEquals((Double) 0.1D, (Double) testAccount.getBalance());
	}
	
	@Test(expected = BadDepositException.class)
	public void testBadDeposit() throws BadDepositException {
		testAccount.makeDeposit(-0.1D);
	}

	@Test
	public void testMakeWithdrawal() {
		try {
			testAccount.makeDeposit(1.0D);
		} catch (BadDepositException e) {
			fail("Deposit before withdrawal treated as bad");
		}
		testAccount.makeWithdrawal(0.1D);
		assertEquals((Double) 0.9D, (Double) testAccount.getBalance());
	}

}
