package org.example.SoapISPService.soap;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.SoapISPService.model.Tariff;

@WebService
public interface TariffSoapInt {
	@WebMethod public Tariff createTariff(String service, double price);
	
	@WebMethod public int removeTariff(long tariffId);

	@WebMethod public Tariff getTariff(long tariffId);

	@WebMethod public List<Tariff> getAllTariffs();
}
