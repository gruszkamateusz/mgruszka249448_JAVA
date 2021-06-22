package org.example.SoapISPService.soap;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.example.SoapISPService.model.Payment;

@WebService
public interface PaymentSoapInt {
	@WebMethod public Payment createPayment(long chargeId, String date, double amount);
	
	@WebMethod public int removePayment(long paymentId);

	@WebMethod public Payment getPayment(long paymentId);

	@WebMethod public List<Payment> getAllPayments();
	
	@WebMethod public List<Payment> getAllPaymentsOfCharge(long chargeId);
}
