package org.example.SoapISPService.model;

import java.io.Serializable;

public class Installation implements Serializable {
	private static final long serialVersionUID = -5217047159262956525L;
	private long installationId;
	private long clientId;
	private String address;
	private long tariffId;

	public Installation() {
		this.installationId = -1;
		this.clientId = -1;
		this.address = "";
		this.tariffId = -1;
	}
	
	public Installation(long installationId, long clientId, String address, 
			long tariffId) {
		this.installationId = installationId;
		this.clientId = clientId;
		this.address = address;
		this.tariffId = tariffId;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public long getId() {
		return installationId;
	}
	
	public void setId(long installationId) {
		this.installationId = installationId;
	}
	
	public long getTariffId() {
		return tariffId;
	}
	
	public void setTariffId(long tariffId) {
		this.tariffId = tariffId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return "installation: " + installationId + "," + clientId + "," +
				address + "," + tariffId;
	}
}
