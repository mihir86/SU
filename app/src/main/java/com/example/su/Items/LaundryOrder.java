package com.example.su.Items;

import android.util.Log;

public class LaundryOrder {

	private String givenDate;
	private int amount;
	private boolean done;

	public LaundryOrder() {
	}

	public LaundryOrder(String givenDate, int amount, boolean done)
	{
		this.givenDate = givenDate;
		this.amount = amount;
		this.done = done;
	}

	public String getGivenDate() {
		return givenDate;
	}

	public int getAmount() {
		return amount;
	}

	public boolean isDone() {
		return done;
	}

	public void setAmount(int amount) {
		Log.e("Setter called for", "amount");
		this.amount = amount;
	}

	public void setGivenDate(String givenDate) {
		Log.e("Setter called for", "date");
		this.givenDate = givenDate;
	}

	public void setDone(boolean done) {
		Log.e("Setter called for", "isDone");
		this.done = done;
	}
}
