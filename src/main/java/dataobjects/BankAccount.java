package dataobjects;

public class BankAccount {
	private int bankAccountid;
	private double balance;
	private int status;
	private int bankAccountType;

	public BankAccount () {
		this.bankAccountid    = 0;
		this.balance          = 0;
		this.status           = 0;
		this.bankAccountType  = 0;
	}

	public int getBankAccountid() {
		return bankAccountid;
	}

	public void setBankAccountid(int accountid) {
		this.bankAccountid = accountid;
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
		return bankAccountType;
	}

	public void setAccounttype(int accounttype) {
		this.bankAccountType = accounttype;
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
