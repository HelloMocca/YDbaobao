package com.ydbaobao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.support.CommonUtil;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Category;
import com.ydbaobao.model.Product;

@Repository
public class ProductDao extends JdbcDaoSupport {
	
	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}
	
	/**
	 * 상품을 생성하고 생성된 상품 productId 반환
	 * @param product
	 * @return 생성된 상품의 productId
	 */
	public int create(final Product product) {
		final String sql = "insert into PRODUCTS values(default, ?, ?, ?, default, ?, default, ?, ?, ?, default)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, new String[] {"productId"});
				ps.setString(1, product.getProductName());
				ps.setObject(2, product.getCategory().getCategoryId());
				ps.setObject(3, product.getBrand().getBrandId());
				ps.setString(4, product.getProductImage());
				ps.setString(5, CommonUtil.getDatetime());
				ps.setString(6, CommonUtil.getDatetime());
				ps.setString(7, product.getProductSize());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	public Product read(int productId) {
		String sql = "select * from PRODUCTS where productId = ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), null, 0, 0, 0, 0, 0, 0, ""),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
			
		};
		return getJdbcTemplate().queryForObject(sql, rm, productId);
	}
	
	public int updateProductImage(int productId, String imageName) {
		String sql = "update PRODUCTS set productImage = ? where productId = ?";
		return getJdbcTemplate().update(sql, imageName, productId);
	}

	public int update(Product product) {
		String sql = "update PRODUCTS set productName = ?, categoryId = ?, brandId = ?, productPrice=?, productDescription=?, productUpdateDate=?, productSize=?, productImage=?, isSoldout = ? where productId = ?";
		return getJdbcTemplate().update(sql, product.getProductName(), product.getCategory().getCategoryId(), product.getBrand().getBrandId(), product.getProductPrice(), product.getProductDescription(), CommonUtil.getDatetime(), product.getProductSize(), product.getProductImage(), product.getIsSoldout(), product.getProductId());
	}

	public List<Product> readListByCategoryId(int categoryId, int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS A, BRANDS B where A.brandId = B.brandId and categoryId=? ORDER BY productId desc limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(sql, rm, categoryId, offset, productsPerPage);
	}
	
	public List<Product> readByProductName(String param, int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS A, BRANDS B WHERE A.brandId = B.brandId and productName REGEXP (?) order by productId desc limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(sql, rm, param, offset, productsPerPage);
	}
	
	public List<Product> readByBrandName(String param, int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS A, BRANDS B WHERE A.brandId = B.brandId and brandName REGEXP (?) order by productId desc limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		
		return getJdbcTemplate().query(
				sql, rm, param, offset, productsPerPage);
	}

	public int count() {
		String sql = "select count(1) from PRODUCTS";
		return getJdbcTemplate().queryForObject(sql, Integer.class);
	}

	public int countBySearchProductName(String param) {
		String sql = "select count(1) as count from PRODUCTS WHERE productName REGEXP (?)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, param);
	}
	
	public int countBySearchBrandName(String param) {
		String sql = "select count(1) as count from PRODUCTS as P, BRANDS as B WHERE P.brandId = B.brandId and brandName REGEXP (?)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, param);
	}
	
	/**
	 * offset부터 productsPerPage만큼 상품 수를 불러온다
	 * @param offset, productsPerPage
	 * @param productsPerPage
	 * @return PRODUCTS table에서 offset에서부터 productsPerPage 만큼 가져온 Products
	 */
	public List<Product> readRange(int offset, int productsPerPage) {
		String sql ="select * from PRODUCTS A, BRANDS B where B.brandId = A.brandId ORDER BY productId desc LIMIT ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(sql, rm, offset, productsPerPage);
	}

	/**
	 * 카테고리의 전체 상품을 페이지에 따라 productsPerPage 수만큼 가져온다
	 * @param categoryId, offset, productsPerPage
	 * @return categoryId가 일치하는 상품들 중 offset부터 productsPerPage 만큼 가져온 Products
	 */
	public List<Product> readListByCategoryId(int categoryId) {
		String sql = "select * from PRODUCTS A, BRANDS B where A.brandId = B.brandId and categoryId=? ORDER BY productId desc";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(
				sql, rm, categoryId);
	}
	
	/**
	 * 브랜드의 전체 상품을 페이지에 따라 productsPerPage 수만큼 가져온다
	 * @param brandId, offset, productsPerPage
	 * @return brandId가 일치하는 상품들 중 offset부터 productsPerPage 만큼 가져온 Products
	 */
	public List<Product> readListByBrandId(int brandId, int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS as A, BRANDS as B where B.brandId = A.brandId and A.brandId=? ORDER BY productId DESC limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0),
						new Brand(rs.getInt("brandId"), rs.getString("brandName"), rs.getInt("brandCount"), rs.getInt("discount_1"), rs.getInt("discount_2"), rs.getInt("discount_3"), rs.getInt("discount_4"), rs.getInt("discount_5"), rs.getString("brandSize")),
						rs.getInt("productPrice"), rs.getString("productImage"),
						rs.getString("productDescription"), rs.getLong("productCreateDate"),
						rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(
				sql, rm, brandId, offset, productsPerPage);
	}

	/**
	 * 카테고리 별 브랜드 상품을 페이지에 따라 productsPerPage 수만큼 가져온다
	 * @param categoryId, brandId, offset, productsPerPage
	 * @return categoryId와 brandId가 일치하는 상품들 중 offset부터 productsPerPage 만큼 가져온 Products
	 */
	public List<Product> readByCategoryIdAndBrandId(int categoryId, int brandId, int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS where categoryId = ? and brandId = ? ORDER BY productId DESC limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0), 
						new Brand(rs.getInt("brandId"), null, 0, 0, 0, 0, 0, 0, ""), 
						rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productDescription"),
						rs.getLong("productCreateDate"), rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(sql, rm, categoryId, brandId, offset, productsPerPage);
	}
	
	public List<Product> readProductsList(int offset, int productsPerPage) {
		String sql = "select * from PRODUCTS ORDER BY productId DESC limit ?, ?";
		RowMapper<Product> rm = new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Product(
						rs.getInt("productId"), rs.getString("productName"),
						new Category(rs.getInt("categoryId"), null, 0), 
						new Brand(rs.getInt("brandId"), null, 0, 0, 0, 0, 0, 0, ""), 
						rs.getInt("productPrice"), rs.getString("productImage"), rs.getString("productDescription"),
						rs.getLong("productCreateDate"), rs.getLong("productUpdateDate"), rs.getString("productSize"), rs.getInt("isSoldout"));
			}
		};
		return getJdbcTemplate().query(sql, rm, offset, productsPerPage);
	}
	
	public int deleteAll() {
		String sql = "delete from PRODUCTS";
		return getJdbcTemplate().update(sql);
	}

	public int delete(Product product) {
		String sql = "delete from PRODUCTS where productId=?";
		return getJdbcTemplate().update(sql, product.getProductId());
	}

	public void resetAutoIncrement() {
		String sql = "alter table PRODUCTS auto_increment=1;";
		getJdbcTemplate().update(sql);
	}

	public void changeUnclassifiedCategory(long categoryId) {
		String sql = "update PRODUCTS set categoryId = 0 where categoryId = ?";
		getJdbcTemplate().update(sql, categoryId);
	}

	public int countProductByBrandIdAndCategoryId(int categoryId, int brandId) {
		String sql = "select count(1) as count from PRODUCTS WHERE categoryId=? and brandId=?";
		return getJdbcTemplate().queryForObject(sql, Integer.class, categoryId, brandId);
	}
}
