package org.example.SoapISPService.soap;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.SoapISPService.model.Installation;

@WebService
public interface InstallationSoapInt {
	@WebMethod public Installation createInstallation(long clientId, String address, long tariffId);
	
	@WebMethod public int removeInstallation(long installationId);

	@WebMethod public Installation getInstallation(long installationId);

	@WebMethod public List<Installation> getAllInstallations();
	
	@WebMethod public List<Installation> getAllInstallationsOfClient(long clientId);
}
