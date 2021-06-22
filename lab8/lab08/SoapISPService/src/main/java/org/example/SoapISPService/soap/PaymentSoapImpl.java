package org.example.SoapISPService.soap;

import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.example.SoapISPService.dao.PaymentDao;
import org.example.SoapISPService.model.Payment;

@WebService(endpointInterface = "org.example.SoapISPService.soap.PaymentSoapInt")
public class PaymentSoapImpl implements PaymentSoapInt {

	@PostConstruct
	private void init() {
		PaymentDao.createTable();
	}

	@WebMethod
	public Payment createPayment(long chargeId, String date, double amount) {
		Payment pay = new Payment(-1, chargeId, LocalDate.parse(date).toString(), amount);
		pay.setId(PaymentDao.insert(pay));
		return pay;
	}

	@WebMethod
	public int removePayment(long paymentId) {
		return PaymentDao.delete(paymentId);
	}

	@WebMethod
	public Payment getPayment(long paymentId) {
		return PaymentDao.get(paymentId);
	}

	@WebMethod
	public List<Payment> getAllPayments() {
		return PaymentDao.getAll();
	}

	@WebMethod
	public List<Payment> getAllPaymentsOfCharge(long chargeId) {
		return PaymentDao.getAllOfChargePayments(chargeId);
	}

}
