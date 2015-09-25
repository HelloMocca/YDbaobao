package com.ydbaobao.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.ydbaobao.model.AdminConfig;

@Repository
public class AdminConfigDao extends JdbcDaoSupport {

	@Resource
	private DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	public AdminConfig read() {
		String sql = "select * from ADMINCONFIG limit 1";
		RowMapper<AdminConfig> rm = new RowMapper<AdminConfig>() {
			@Override
			public AdminConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AdminConfig(rs.getInt("adminConfigId"),
						rs.getInt("adminDisplayProducts"),
						rs.getString("adminPassword"),
						rs.getInt("adminCostPerWeight"));
			}
		};
		return getJdbcTemplate().queryForObject(sql, rm);
	}

	public void update(AdminConfig adminConfig) {
		String sql = "update ADMINCONFIG set adminDisplayProducts = ?, adminPassword = ?";
		getJdbcTemplate().update(sql, adminConfig.getAdminDisplayProducts(), adminConfig.getAdminPassword());
	}
}
