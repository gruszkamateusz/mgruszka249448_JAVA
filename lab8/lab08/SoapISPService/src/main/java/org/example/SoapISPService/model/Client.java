package org.example.SoapISPService.model;

import java.io.Serializable;

public class Client implements Serializable {
	private static final long serialVersionUID = -8473542233089581512L;
	private String name;
	private String firstName;
	private long id;

	public Client() {
		this.firstName = "";
		this.name = "";
		this.id = -1;
	}
	
	public Client(long id, String name, String firstName) {
		this.firstName = firstName;
		this.name = name;
		this.id = id;
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

	public void setId(long id) {
		this.id = id;
	}
}
