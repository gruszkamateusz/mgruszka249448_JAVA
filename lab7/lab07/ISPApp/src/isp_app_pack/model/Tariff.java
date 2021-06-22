package isp_app_pack.model;

public class Tariff {
	public static long nextTariffId = 0;
	private long id;
	private String service;
	private double price;
	
	public Tariff(String service, double price) {
		this.service = service;
		this.price = price;
		this.id = nextTariffId++;
	}
	
	public Tariff(long id, String service, double price) {
		this.service = service;
		this.price = price;
		this.id = id;
	}

	public static long getNextId() {
		return nextTariffId++;
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
