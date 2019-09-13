package com.example.su.Items;

public class LaundryOrder {

	private String givenDate;
	private double amount;
	private boolean done;

	public LaundryOrder(String givenDate, double amount, boolean done)
	{
		this.givenDate = givenDate;
		this.amount = amount;
		this.done = done;
	}

	public String getGivenDate() {
		return givenDate;
	}

	public double getAmount() {
		return amount;
	}

	public boolean isDone() {
		return done;
	}
}
