package isp_app_pack.model;

import java.time.LocalDate;

public class Payment {
	public static long nextPaymentId = 0;
	private long id;
	private Charge charge;
	private LocalDate date;
	private double amount;
	
	public Payment(Charge charge, LocalDate date, double amount) {
		this.id = nextPaymentId++;
		this.charge = charge;
		this.date = date;
		this.amount = amount;
	}
	
	public Payment(long id, Charge charge, LocalDate date, double amount) {
		this.id = id;
		this.charge = charge;
		this.date = date;
		this.amount = amount;
	}
	
	public static long getNextId() {
		return nextPaymentId++;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Charge getCharge() {
		return charge;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

	@Override
	public String toString() {
		return "payment: " + id + "," + charge.getId() + "," + date + "," + amount;
	}
}
