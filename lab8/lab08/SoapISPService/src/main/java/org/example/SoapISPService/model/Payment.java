package org.example.SoapISPService.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Payment implements Serializable {
	private static final long serialVersionUID = 8884592361951056426L;
	private long id;
	private long chargeId;
	private String date;
	private double amount;

	public Payment() {
		this.id = -1;
		this.chargeId = -1;
		this.date = LocalDate.now().toString();
		this.amount = 0;
	}
	
	public Payment(long id, long chargeId, String date, double amount) 
			throws DateTimeParseException{
		this.id = id;
		this.chargeId = chargeId;
		this.date = LocalDate.parse(date).toString();
		this.amount = amount;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) throws DateTimeParseException {
		this.date = LocalDate.parse(date).toString();
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getChargeId() {
		return chargeId;
	}

	public void setChargeId(long chargeId) {
		this.chargeId = chargeId;
	}

	@Override
	public String toString() {
		return "payment: " + id + "," + chargeId + "," + date + "," + amount;
	}
}
