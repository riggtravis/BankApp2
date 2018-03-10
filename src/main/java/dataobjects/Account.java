package dataobjects;

public class Account {
	private int accountid;
	private double balance;
	private int status;
	private int accounttype;

	public Account () {
		this.accountid		= 0;
		this.balance			= 0;
		this.status 			= 0;
		this.accounttype	= 0;
	}

	public int getAccountid() {
		return accountid;
	}

	public void setAccountid(int accountid) {
		this.accountid = accountid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(int accounttype) {
		this.accounttype = accounttype;
	}

	public void makeDeposit(double deposit) throws BadTransactionException {
		// Make sure the user doesn't try to make a negative deposit
		if (deposit < 0) {
			throw new BadTransactionException();
		}
		this.balance += deposit;
	}

	public void makeWithdrawal(double withdrawal) throws BadTransactionException {
	  // Make sure the user doesn't try to make a negative withdrawal
	  if (withdrawal < 0) {
	    throw new BadTransactionException();
	  }
		this.balance -= withdrawal;
	}

}
