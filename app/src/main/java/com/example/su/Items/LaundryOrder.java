package com.example.su.Items;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LaundryOrder {

	private Date givenDate;
	private double amount;
	private boolean done;

	public LaundryOrder(Date givenDate, double amount, boolean done)
	{
		this.givenDate = givenDate;
		this.amount = amount;
		this.done = done;
	}

	public String getGivenDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yy", Locale.getDefault());
		return simpleDateFormat.format(givenDate);
	}

	public double getAmount() {
		return amount;
	}

	public boolean isDone() {
		return done;
	}

}
