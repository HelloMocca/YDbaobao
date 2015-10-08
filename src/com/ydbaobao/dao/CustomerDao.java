package com.ydbaobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.support.CommonUtil;
import com.ydbaobao.model.Customer;

@Repository
public class CustomerDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;
	
	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	public void createCustomer(Customer customer) {
		String sql = "insert into CUSTOMERS values(?, ?, ?, default, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(sql, customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerPassword(), customer.getCustomerPhone(), customer.getCustomerEmail(), customer.getCustomerAddress(), CommonUtil.getDatetime(), CommonUtil.getDatetime());
	}
	
	public Customer findCustomerByCustomerId(String customerId) {
		String sql = "select * from CUSTOMERS where customerId=?";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress")
						);
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rm, customerId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int updateCustomer(Customer customer) {
		String sql = "update CUSTOMERS set customerName = ?, customerPassword = ?, customerPhone = ?, customerEmail = ?, customerAddress = ?, customerUpdateDate = ? where customerId = ?";
		return getJdbcTemplate().update(sql, customer.getCustomerName(), customer.getCustomerPassword(), customer.getCustomerPhone(), customer.getCustomerEmail(), customer.getCustomerAddress(), CommonUtil.getDatetime(), customer.getCustomerId());		
	}
	
	public int updateCustomerGrade(Customer customer) {
		String sql = "update CUSTOMERS set customerGrade = ? where customerId = ?";
		return getJdbcTemplate().update(sql, customer.getCustomerGrade(), customer.getCustomerId());		
	}
	
	public List<Customer> readCustomers() {
		String sql = "select * from CUSTOMERS order by customerCreateDate DESC";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress")
						);
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public Customer readCustomerById(String customerId) {
		String sql = "select *from CUSTOMERS where customerId = ?";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress"),
						rs.getString("customerCreateDate")
						);
			}
		};
		return getJdbcTemplate().queryForObject(sql, rm, customerId);
	}

	/**
	 * 유저 계정 삭제(비활성화)
	 * 결제 내역을 보관하기 위해 유저 계정은 등급 0으로 비활성화
	 * @param customerId
	 */
	public void deleteCustomer(String customerId) {
		String sql = "update CUSTOMERS set customerGrade = 0, customerPhone='', customerEmail='', customerAddress='', customerUpdateDate=? where customerId = ?";
		getJdbcTemplate().update(sql, CommonUtil.getDatetime(), customerId);
		sql = "delete from ITEMS where customerId = ?";
		getJdbcTemplate().update(sql, customerId);
	}
	
	/**
	 * offset부터 customersPerPage만큼 상품 수를 불러온다
	 * @param offset, customersPerPage
	 * @param customersPerPage
	 * @return CUSTOMERS table에서 offset에서부터 customersPerPage 만큼 가져온 Customers
	 */
	public List<Customer> readRange(String termsForQuery, int page, int customersPerPage) {
		String sql ="select * from CUSTOMERS where customerName REGEXP (?) ORDER BY customerName LIMIT ?, ?";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress"),
						rs.getString("customerCreateDate")
						);
			}
		};
		return getJdbcTemplate().query(sql, rm, termsForQuery, page, customersPerPage);
	}

	public int countBySearchCustomerName(String termsForQuery) {
		String sql = "select count(1) as count from CUSTOMERS where customerName REGEXP (?)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, termsForQuery);
	}

	public int countCustomers() {
		String sql = "select count(1) as count from CUSTOMERS";
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	public List<Customer> readCustomers(int page, int customersPerPage) {
		String sql ="select * from CUSTOMERS ORDER BY customerCreateDate DESC LIMIT ?, ?";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress"),
						rs.getString("customerCreateDate")
						);
			}
		};
		return getJdbcTemplate().query(
				sql,rm, page, customersPerPage);
	}

	public int countBySearchCustomerId(String terms) {
		String sql = "select count(1) as count from CUSTOMERS where customerId REGEXP (?)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, terms);
	}

	public List<Customer> readBySerchCustomerId(String termsForQuery, int page, int customersPerPage) {
		String sql ="select * from CUSTOMERS where customerId REGEXP (?)  ORDER BY customerCreateDate DESC LIMIT ?, ?";
		RowMapper<Customer> rm = new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Customer(
						rs.getString("customerId"), 
						rs.getString("customerName"), 
						rs.getString("customerPassword"),
						rs.getString("customerGrade"),
						rs.getString("customerPhone"),
						rs.getString("customerEmail"),
						rs.getString("customerAddress"),
						rs.getString("customerCreateDate")
						);
			}
		};
		return getJdbcTemplate().query(
				sql, rm, termsForQuery, page, customersPerPage);
	}
}
