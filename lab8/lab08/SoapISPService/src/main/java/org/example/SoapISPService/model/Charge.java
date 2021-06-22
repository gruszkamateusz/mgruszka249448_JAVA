package org.example.SoapISPService.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Charge implements Serializable {
	private static final long serialVersionUID = -5184429076902612481L;
	private long id;
	private long installationId;
	private String date;
	private double amount;

	public Charge() {
		this.id = -1;
		this.installationId = -1;
		this.date = LocalDate.now().toString();
		this.amount = 0;
	}
	
	public Charge(long id, long installationId, String date, double amount) 
			throws DateTimeParseException {
		this.id = id;
		this.installationId = installationId;
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

	public long getInstallationId() {
		return installationId;
	}

	public void setInstallationId(long installationId) {
		this.installationId = installationId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "charge: " + id + "," + installationId + "," + date + "," + amount;
	}
}
