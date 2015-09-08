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

import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Customer;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Payment;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.Quantity;

@Repository
public class ItemDao extends JdbcDaoSupport {
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public int createItem(final String customerId, final int productId, final String itemStatus) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into ITEMS (customerId, productId, itemStatus, price) values(?, ?, ?, 0)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, customerId);
				ps.setInt(2, productId);
				ps.setString(3, itemStatus);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public int createItem(final String customerId, final int productId, final String size, final int quantity, final String itemStatus, final int price,  final int paymentId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into ITEMS (customerId, productId, size, quantity, itemStatus, price, paymentId) values(?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, customerId);
				ps.setInt(2, productId);
				ps.setString(3, size);
				ps.setInt(4, quantity);
				ps.setString(5, itemStatus);
				ps.setInt(6, price);
				ps.setInt(7, paymentId);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	public List<Item> readCartItems(String customerId) {
		String sql = "select * from ITEMS A, PRODUCTS B, CUSTOMERS C, BRANDS D where A.itemStatus = '"+Item.CART+"' AND A.customerId= ? AND A.productId = B.productId AND A.customerId = C.customerId AND B.brandId = D.brandId order by A.productId";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(rs.getInt("itemId"),
						new Customer(rs.getString("customerId"), rs.getString("customerName"), rs.getString("gradeId")),
						new Product(rs.getInt("productId"), rs.getString("productName"), rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productSize"), rs.getInt("isSoldout"),
								new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("discount_1"), rs.getInt("discount_2"),
										rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"))),
						rs.getString("itemStatus"), rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().query(
					sql, rm, customerId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Item> readOrderedItems() {
		String sql = "select * from ITEMS A, PRODUCTS B where A.itemStatus = '"+Item.ORDERED+"' AND A.quantity != 0 AND A.productId = B.productId order by A.itemId";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productSize"), rs.getInt("isSoldout"), new Brand(rs.getInt("brandId"))),
						rs.getString("itemStatus"), rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public Item readItem(int itemId) {
		String sql = "select * from ITEMS A, PRODUCTS B where A.itemId=? AND A.productId = B.productId";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productSize"), rs.getInt("isSoldout"), new Brand(rs.getInt("brandId"))),
						rs.getString("itemStatus"), rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rm, itemId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
//	public Item readItemByProductIdAndSizeAndItemStatus(int productId, String size, String customerId, String itemStatus) {
//		String sql = "select * from ITEMS where productId = ? and size = ? and customerId = ? and itemStatus = ?";
//		RowMapper<Item> rm = new RowMapper<Item>() {
//			@Override
//			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
//				return new Item(
//						rs.getInt("itemId"), new Customer(rs.getString("customerId")),
//						new Product(rs.getInt("productId")),
//						rs.getString("itemStatus"), rs.getInt("price"));
//			}
//		};
//		try {
//			return getJdbcTemplate().queryForObject(sql, rm, productId, size, customerId, itemStatus);
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		}
//	}

	public void updateItemPrice(int itemId, int price) {
		String sql = "update ITEMS set price = ? where itemId = ?";
		getJdbcTemplate().update(sql, price, itemId);
	}

	public List<Item> readOrderedItemsByCustomerId(String customerId) {
		String sql = "select * from ITEMS A, PRODUCTS B where A.customerId = ? "
				+ "AND A.productId = B.productId "
				+ "AND A.itemStatus = 'S'";
		
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"))), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm, customerId);
	}

	public List<Item> readItemsByCustomerId(String customerId) {
		String sql = "select * from ITEMS where customerId = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId")), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm, customerId);
	}
	
	public List<Item> readItemsByProductId(int productId) {
		String sql  = "select * from ITEMS A, PRODUCTS B where A.productId = ? AND A.productId = B.productId";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"))), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm, productId);
	}
	
	public List<Item> readItemsByBrandId(int brandId) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.productId = B.productId AND B.brandId = C.brandId AND C.brandId = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"), rs.getString("brandName"))), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm, brandId);
	}
	
	public List<Item> readOrderedItemsByBrandId(int brandId) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = 'S' AND A.productId = B.productId AND B.brandId = C.brandId AND C.brandId = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"), rs.getString("brandName"))), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm, brandId);
	}

	public List<Item> readOrderedItemsOrderBy(String arg) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = 'S' AND A.productId = B.productId AND B.brandId = C.brandId";
		if (arg.equals("customerId")) {
			sql += " ORDER BY A.customerId desc";
		} else { //Default
			sql  += " ORDER BY B.brandId desc";
		}
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"), rs.getString("brandName"))), rs.getString("itemStatus"), 
						rs.getInt("price"));
			}
		};
		return getJdbcTemplate().query(sql, rm);
	}

	public List<Item> readItemsByPaymentId(int paymentId) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C, PAYMENTS D where A.itemStatus = 'P' AND A.productId = B.productId AND B.brandId = C.brandId AND A.paymentId = D.paymentId AND D.paymentId = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"), rs.getString("brandName"))), rs.getString("itemStatus"), 
						rs.getInt("price"), new Payment(rs.getInt("paymentId"), new Customer(rs.getString("customerId")), rs.getString("paymentType"), rs.getInt("amount"), rs.getString("paymentDate")));
			}
		};
		return getJdbcTemplate().query(sql, rm, paymentId);
	}

	public List<Item> readItemByItemIds(String[] itemIds) {
		String condition = "";
		for (int i = 0; i < itemIds.length; i++) {
			if (i != 0) condition += " OR ";
			condition += "A.itemId = "+itemIds[i];
		}
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = 'S' AND A.productId = B.productId AND B.brandId = C.brandId AND ( "+condition+" )";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), 
						new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), 
								rs.getInt("productPrice"), rs.getString("productImage"), 
								rs.getString("productSize"), rs.getInt("isSoldout"), 
						new Brand(rs.getInt("brandId"), rs.getString("brandName"))), rs.getString("itemStatus"), 
						rs.getInt("price"), null);
			}
		};
		return getJdbcTemplate().query(sql, rm);
	}

	public Item readItemByCustomerIdAndProductIdAndItemStatus(String customerId, int productId, String itemStatus) {
		String sql = "select * from ITEMS where customerId = ? and productId = ? and itemStatus = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId")),
						rs.getString("itemStatus"), rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rm, customerId, productId, itemStatus);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * Item의 상태를 변환
	 * 카트 		: "I"
	 * 주문요청 	: "S"
	 * 사입처리         : "P"
	 * 취소 		: "C"
	 * 반려 		: "R"
	 * @param itemId
	 * @param itemStatus
	 */
	public void updateItemStatus(int itemId, String itemStatus) {
		String sql = "update ITEMS set itemStatus = ? where itemId = ?";
		getJdbcTemplate().update(sql, itemStatus, itemId);
	}

	public int deleteItem(int itemId) {
		String sql = "delete from ITEMS where itemId = ?";
		return getJdbcTemplate().update(sql, itemId);
	}

	public void addItemQuantity(int itemId, String size, int quantity) {
		String sql = "update QUANTITY set value = value + ? where itemId = ? AND size = ?";
		getJdbcTemplate().update(sql, quantity, itemId, size);
	}
	
	public int updateItemQuantity(int quantityId, int quantity) {
		if (quantity == 0) {
			return deleteQuantity(quantityId);
		}
		String sql = "update QUANTITY set value = ? where quantityId = ?";
		return getJdbcTemplate().update(sql, quantity, quantityId);
	}
	
	public List<Quantity> readQuantityByItemId(int itemId) {
		String sql = "select * from QUANTITY where itemId = ?";
		RowMapper<Quantity> rm = new RowMapper<Quantity>() {
			@Override
			public Quantity mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Quantity(
							rs.getInt("quantityId"),
							rs.getInt("itemId"),
							rs.getString("size"),
							rs.getInt("value")
						);
			}
		};
		return getJdbcTemplate().query(sql, rm, itemId);
	}
	
	public Quantity readQuantityByItemIdAndSize(int itemId, String size) {
		String sql = "select * from QUANTITY where itemId = ? and size = ?";
		RowMapper<Quantity> rm = new RowMapper<Quantity>() {
			@Override
			public Quantity mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Quantity(
							rs.getInt("quantityId"),
							rs.getInt("itemId"),
							rs.getString("size"),
							rs.getInt("value")
						);
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rm, itemId, size);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public int createQuantity(final Quantity quantity) {
		System.out.println("Quantity 생성: "+quantity.toString());
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into QUANTITY (itemId, size, value) values(?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, quantity.getItemId());
				ps.setString(2, quantity.getSize());
				ps.setInt(3, quantity.getValue());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public int deleteQuantity(int quantityId) {
		String sql = "delete from QUANTITY where quantityId = ?";
		return getJdbcTemplate().update(sql);
	}
}
