package isp_app_pack.model;

import java.time.LocalDate;

public class Charge {
	public static long nextChargeId = 0;
	private long id;
	private Installation installation;
	private LocalDate date;
	private double amount;

	public Charge(Installation installation, LocalDate date, double amount) {
		this.id = nextChargeId++;
		this.installation = installation;
		this.date = date;
		this.amount = amount;
	}
	
	public Charge(long id, Installation installation, LocalDate date, double amount) {
		this.id = id;
		this.installation = installation;
		this.date = date;
		this.amount = amount;
	}

	public static long getNextId() {
		return nextChargeId++;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Installation getInstallation() {
		return installation;
	}

	public void setInstallationId(Installation installation) {
		this.installation = installation;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "charge: " + id + "," + installation.getId() + "," + date + "," + amount;
	}
}
