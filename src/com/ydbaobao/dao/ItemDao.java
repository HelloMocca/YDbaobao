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
			return getJdbcTemplate().query(sql, rm, customerId);
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
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.productId = B.productId AND B.brandId = C.brandId AND A.itemId = ?";
		RowMapper<Item> rm = new RowMapper<Item>() {
			@Override
			public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Item(
						rs.getInt("itemId"), new Customer(rs.getString("customerId")),
						new Product(rs.getInt("productId"),rs.getString("productName"), rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productSize"), rs.getInt("isSoldout"), new Brand(rs.getInt("brandId"), rs.getString("brandName"))),
						rs.getString("itemStatus"), rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().queryForObject(sql, rm, itemId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		try {
			return getJdbcTemplate().query(sql, rm, customerId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		try {
			return getJdbcTemplate().query(sql, rm, customerId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		try {
			return getJdbcTemplate().query(sql, rm, productId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		try {
			return getJdbcTemplate().query(sql, rm, brandId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Item> readOrderedItemsByBrandId(int brandId) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = '"+Item.ORDERED+"' AND A.productId = B.productId AND B.brandId = C.brandId AND C.brandId = ?";
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
		try {
			return getJdbcTemplate().query(sql, rm, brandId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Item> readOrderedItemsOrderBy(String arg) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = '"+Item.ORDERED+"' AND A.productId = B.productId AND B.brandId = C.brandId";
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
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Item> readItemsByOrderId(int orderId) {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.itemStatus = 'P' AND A.productId = B.productId AND B.brandId = C.brandId AND A.orderId = ?";
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
		try {
			return getJdbcTemplate().query(sql, rm, orderId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
						rs.getInt("price"));
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Item> readOrderedProductByItemIds(String[] itemIds) {
		String condition = "";
		for (int i = 0; i < itemIds.length; i++) {
			if (i != 0) condition += " OR ";
			condition += "A.itemId = "+itemIds[i];
		}
		String sql = "select *, sum(A.value) as value from QUANTITY A, ITEMS B, PRODUCTS C, BRANDS D where B.itemStatus = 'S' AND B.productId = C.productId AND C.brandId = D.brandId AND ( "+condition+" ) group by B.productId, A.size";
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
						rs.getInt("price"),
						new Quantity(rs.getInt("quantityId"), rs.getInt("itemId"), rs.getString("size"), rs.getInt("value")));
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Quantity> readOrderedItemQuantityByItemId(int itemId) {
		String sql = "select *, sum(A.value) as value from QUANTITY A, ITEMS B, PRODUCTS C, BRANDS D where B.itemStatus = 'S' AND B.productId = C.productId AND C.brandId = D.brandId AND A.itemId=? group by B.productId, A.size";
		RowMapper<Quantity> rm = new RowMapper<Quantity>() {
			@Override
			public Quantity mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Quantity(rs.getInt("quantityId"), rs.getInt("itemId"), rs.getString("size"), rs.getInt("value"));
			}
		};
		try {
			return getJdbcTemplate().query(sql, rm, itemId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
	
	public Item readItemByQuantityId(int quantityId) {
		String sql = "select * from ITEMS A, QUANTITY B where A.itemId = B.itemId AND B.quantityId = ?";
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
		try {
			return getJdbcTemplate().queryForObject(sql, rm, quantityId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	/**
	 * Item을 배송상태로 전환
	 * @param itemId
	 * @param orderId
	 * @return
	 */
	public boolean updateItemToShipmentStatus(int itemId, int orderId) {
		String sql = "update ITEMS set itemStatus = '"+Item.SHIPMENT+"', orderId = ? where itemId = ?";
		return (getJdbcTemplate().update(sql, orderId, itemId) == 1) ? true : false;
	}
	/**
	 * Item의 상태를 변환
	 * 카트 		: "I"
	 * 주문요청 	: "S"
	 * 사입처리   : "P"
	 * 취소 		: "C"
	 * 반려 		: "R"
	 * @param itemId
	 * @param itemStatus
	 */
	public boolean updateItemStatus(int itemId, String itemStatus) {
		String sql = "update ITEMS set itemStatus = ? where itemId = ?";
		return (getJdbcTemplate().update(sql, itemStatus, itemId) == 1) ? true : false;
	}
	
	public boolean updateItemPrice(int itemId, int price) {
		String sql = "update ITEMS set price = ? where itemId = ?";
		return (getJdbcTemplate().update(sql, price, itemId) == 1) ? true : false;
	}

	public boolean deleteItem(int itemId) {
		String sql = "delete from ITEMS where itemId = ?";
		return (getJdbcTemplate().update(sql, itemId) == 1) ? true : false;
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

	public boolean addItemQuantity(int itemId, String size, int quantity) {
		String sql = "update QUANTITY set value = value + ? where itemId = ? AND size = ?";
		return (getJdbcTemplate().update(sql, quantity, itemId, size) == 1) ? true : false;
	}
	
	public boolean updateItemQuantity(int quantityId, int quantity) {
		if (quantity == 0) {
			return deleteQuantity(quantityId);
		}
		String sql = "update QUANTITY set value = ? where quantityId = ?";
		return (getJdbcTemplate().update(sql, quantity, quantityId) == 1) ? true : false;
	}
	
	public Quantity readQuantityByQuantityId(int quantityId) {
		String sql = "select * from QUANTITY where quantityId = ?";
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
			return getJdbcTemplate().queryForObject(sql, rm, quantityId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
		try {
			return getJdbcTemplate().query(sql, rm, itemId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
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
	
	public boolean deleteQuantity(int quantityId) {
		Quantity quantity = readQuantityByQuantityId(quantityId);
		String sql = "delete from QUANTITY where quantityId = ?";
		if (getJdbcTemplate().update(sql, quantityId) == 1) {
			//Item 에 남은 Quantity 가 있는지 확인
			return (readQuantityByItemId(quantity.getItemId()) == null) ? deleteItem(quantity.getItemId()) : true;
		}
		return false;
	}

	public boolean updateQuantity(Quantity quantity) {
		String sql = "update QUANTITY set (itemId = ?, size = ?, value = ?) where quantityId = ?";
		return (getJdbcTemplate().update(sql, quantity.getItemId(), quantity.getSize(), quantity.getValue(), quantity.getQuantityId()) == 1) ? true : false;
	}

	public List<Item> readAcceptedItems() {
		String sql = "select * from ITEMS A, PRODUCTS B, BRANDS C where A.productId = B.productId AND B.brandId = C.brandId AND A.itemStatus = '"+Item.ACCEPTED+"' order by A.customerId";
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
		try {
			return getJdbcTemplate().query(sql, rm);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
