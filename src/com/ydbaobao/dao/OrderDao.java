package com.ydbaobao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.support.CommonUtil;
import com.ydbaobao.model.Order;

@Repository
public class OrderDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public int createOrder(final Order order) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into ORDERS (customerId, shippingCost, extraDiscount, orderPrice, paiedPrice, orderDate) values(?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, order.getCustomerId());
				ps.setInt(2, order.getShippingCost());
				ps.setInt(3, order.getExtraDiscount());
				ps.setInt(4, order.getOrderPrice());
				ps.setInt(5, order.getPaiedPrice());
				ps.setString(6, CommonUtil.getDatetime());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public List<Order> readOrdersByDate(String date) {
		if (date.equals("")) {
			date = CommonUtil.getDate();
		}
		String sql = "select * from ORDERS where orderDate BETWEEN '"+date+" 00:00:00' AND '"+date+" 23:59:59'";
		RowMapper<Order> rm = new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Order(
						rs.getInt("orderId"),
						rs.getString("customerId"),
						rs.getInt("shippingCost"),
						rs.getInt("extraDiscount"),
						rs.getInt("orderPrice"),
						rs.getInt("paiedPrice"),
						rs.getInt("recallPrice"),
						rs.getString("orderDate").split(" ")[0]);
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Order> readOrdersByCustomerId(String customerId) {
		String sql = "select * from ORDERS where customerId = ?";
		RowMapper<Order> rm = new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Order(
						rs.getInt("orderId"),
						rs.getString("customerId"),
						rs.getInt("shippingCost"),
						rs.getInt("extraDiscount"),
						rs.getInt("orderPrice"),
						rs.getInt("paiedPrice"),
						rs.getInt("recallPrice"),
						rs.getString("orderDate").split(" ")[0]);
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm, customerId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int deleteOrderByOrderId(int orderId) {
		String sql = "delete from ORDERS where orderId = ?";
		return getJdbcTemplate().update(sql, orderId);
	}
}
