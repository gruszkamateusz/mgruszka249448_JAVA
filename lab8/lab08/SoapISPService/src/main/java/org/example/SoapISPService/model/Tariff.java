package org.example.SoapISPService.model;

import java.io.Serializable;

public class Tariff implements Serializable {
	private static final long serialVersionUID = -459393741800019111L;
	private long id;
	private String service;
	private double price;

	public Tariff() {
		this.service = "";
		this.price = 0;
		this.id = -1;
	}
	
	public Tariff(long id, String service, double price) {
		this.service = service;
		this.price = price;
		this.id = id;
	}
	
	public String getService() {
		return service;
	}
	
	public void setService(String service) {
		this.service = service;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "tariff: " + id + "," + service + "," + price;
	}	
}
