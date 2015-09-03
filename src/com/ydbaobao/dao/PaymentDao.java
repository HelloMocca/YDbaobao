package com.ydbaobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.support.CommonUtil;
import com.ydbaobao.model.Customer;
import com.ydbaobao.model.Payment;

@Repository
public class PaymentDao extends JdbcDaoSupport {

	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	private int getRandomId() {
		return (int) (Math.random() * 2000000000) + 1;
	}
	
	public int createPayment(Payment payment) {
		String sql = "insert into PAYMENTS value(default, ?, ?, ?, ?)";
		if (payment.getPaymentDate() != null) {
			sql = "insert into PAYMENTS value(default, ?, ?, ?, ?)";
			return getJdbcTemplate().update(sql, payment.getCustomer().getCustomerId(), payment.getPaymentType(), payment.getAmount(), payment.getPaymentDate());
		}
		return getJdbcTemplate().update(sql, payment.getCustomer().getCustomerId(), payment.getPaymentType(), payment.getAmount(), CommonUtil.getDate());
	}
	
	public int updatePayment(Payment payment) {
		String sql = "update PAYMENTS set paymentType = ?, amount = ? where paymentId = ?";
		return getJdbcTemplate().update(sql, payment.getPaymentType(), payment.getAmount(), payment.getPaymentId());
	}

	public List<Payment> readPaymentsByCustomerId(String customerId) {
		String sql = "select *, DATE_FORMAT(PAYMENTS.paymentDate, '%Y-%c-%e') as paymentDate from PAYMENTS where customerId = ?";
		RowMapper<Payment> rm = new RowMapper<Payment>() {
			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Payment(
						rs.getInt("paymentId"),
						new Customer(rs.getString("customerId")),
						rs.getString("paymentType"), 
						rs.getInt("amount"),
						rs.getString("paymentDate"));
			}
		};
		return getJdbcTemplate().query(sql, rm, customerId);
	}
	
	public Payment readPaymentByCustomerIdPaymentDate(String customerId, String date) {
		String sql = "select *, DATE_FORMAT(PAYMENTS.paymentDate, '%Y-%c-%e') as paymentDate from PAYMENTS where customerId = ? AND paymentDate = ?";
		RowMapper<Payment> rm = new RowMapper<Payment>() {
			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Payment(
						rs.getInt("paymentId"),
						new Customer(rs.getString("customerId")),
						rs.getString("paymentType"), 
						rs.getInt("amount"),
						rs.getString("paymentDate"));
			}
		};
		List<Payment> payments = getJdbcTemplate().query(sql, rm, customerId, date);
		if (payments.size() == 0) {
			createPayment(new Payment(new Customer(customerId), "S", 0, date));
			payments = getJdbcTemplate().query(sql, rm, customerId, date);
		}
		return payments.get(0);
	}
}