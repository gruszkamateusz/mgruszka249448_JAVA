package isp_app_pack.model;

public class Installation {
	public static long nextRouterId = 0;
	private long routerId;
	private Client client;
	private String address;
	private Tariff tariff;
	
	public Installation(long routerId, Client client, String address, Tariff tariff) {
		this.routerId = routerId;
		this.client = client;
		this.address = address;
		this.tariff = tariff;
	}
	
	public Installation(Client client, String address, Tariff tariff) {
		this.routerId = nextRouterId++;
		this.client = client;
		this.address = address;
		this.tariff = tariff;
	}
	
	public static long getNextId() {
		return nextRouterId++;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public long getId() {
		return routerId;
	}
	
	public void setRouterId(long routerId) {
		this.routerId = routerId;
	}
	
	public Tariff getTariff() {
		return tariff;
	}
	
	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "installation: " + routerId + "," + client.getId() + "," + address + "," + tariff.getId();
	}
}
