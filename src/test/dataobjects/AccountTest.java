package dataobjects;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;

public class AccountTest {
	Account testAccount;

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
		testAccount.makeDeposit(0.1D);
		assertEquals((Double) 0.1D, (Double) testAccount.getBalance());
	}

}
