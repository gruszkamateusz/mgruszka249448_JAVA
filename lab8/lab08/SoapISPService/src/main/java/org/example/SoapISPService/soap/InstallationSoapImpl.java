package org.example.SoapISPService.soap;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.SoapISPService.dao.InstallationDao;
import org.example.SoapISPService.model.Installation;

@WebService(endpointInterface = "org.example.SoapISPService.soap.InstallationSoapInt")
public class InstallationSoapImpl implements InstallationSoapInt {

	@PostConstruct
	private void init() {
		InstallationDao.createTable();
	}

	@WebMethod
	public Installation createInstallation(long clientId, String address,
			long tariffId) {
		Installation inst = new Installation(-1, clientId, address, tariffId);
		inst.setId(InstallationDao.insert(inst));
		return inst;
	}

	@WebMethod
	public int removeInstallation(long installationId) {
		return InstallationDao.delete(installationId);
	}

	@WebMethod
	public Installation getInstallation(long installationId) {
		return InstallationDao.get(installationId);
	}

	@WebMethod
	public List<Installation> getAllInstallations() {
		return InstallationDao.getAll();
	}

	@WebMethod
	public List<Installation> getAllInstallationsOfClient(long clientId) {
		return InstallationDao.getAllOfClientInstalls(clientId);
	}
}
