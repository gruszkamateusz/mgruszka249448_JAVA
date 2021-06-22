package org.example.SoapISPService.soap;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.SoapISPService.dao.TariffDao;
import org.example.SoapISPService.model.Tariff;

@WebService(endpointInterface = "org.example.SoapISPService.soap.TariffSoapInt")
public class TariffSoapImpl implements TariffSoapInt {

	@PostConstruct
	private void init() {
		TariffDao.createTable();
	}

	@WebMethod
	public Tariff createTariff(String service, double price) {
		Tariff tariff = new Tariff(-1, service, price);
		tariff.setId(TariffDao.insert(tariff));
		return tariff;
	}

	@WebMethod
	public int removeTariff(long tariffId) {
		return TariffDao.delete(tariffId);
	}

	@WebMethod
	public Tariff getTariff(long tariffId) {
		return TariffDao.get(tariffId);
	}

	@WebMethod
	public List<Tariff> getAllTariffs() {
		return TariffDao.getAll();
	}
}
