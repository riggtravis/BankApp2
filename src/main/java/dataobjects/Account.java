package dataobjects;

public class Account {
	private int accountid;
	private double balance;
	private int status;
	private int accounttype;
	
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
}
