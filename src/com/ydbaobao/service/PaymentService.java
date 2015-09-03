package com.ydbaobao.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.PaymentDao;
import com.ydbaobao.model.Payment;

@Service
@Transactional
public class PaymentService {
	@Resource
	PaymentDao paymentDao;
	
	public List<Payment> readPaymentsByCustomerId(String customerId) {
		return paymentDao.readPaymentsByCustomerId(customerId);
	}
	
	public boolean createPayment(Payment payment) {
		if (paymentDao.createPayment(payment) == 1) {
			return true;
		}
		return false;
	}

	/**
	 * customerId와 date가 일치하는 Payment를 반환(해당 날짜의 해당 구매자의 Payment는 단 하나만 존재)
	 * @param customerId
	 * @param date
	 * @return 조건에 일치하는 Payment 하나를 반환 없을경우 조건에 맞는 Payment를 하나 생성해서 반환
	 */
	public Payment readPaymentByCustomerIdDate(String customerId, String date) {
		return paymentDao.readPaymentByCustomerIdPaymentDate(customerId, date);
	}
	
	public int updatePayment(Payment payment) {
		return paymentDao.updatePayment(payment);
	}
}
