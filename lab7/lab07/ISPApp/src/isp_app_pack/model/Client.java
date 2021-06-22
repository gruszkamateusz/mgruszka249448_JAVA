package isp_app_pack.model;

public class Client {
	public static long nextClientId = 0;
	private String name;
	private String firstName;
	private long id;
	
	public Client(String name, String firstName) {
		this.firstName = firstName;
		this.name = name;
		id = nextClientId++;
	}
	
	public Client(long id, String name, String firstName) {
		this.firstName = firstName;
		this.name = name;
		this.id = id;
	}

	public static long getNextId() {
		return nextClientId++;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "client: " + id + ","+ firstName + "," + name;
	}
}
