package com.ydbaobao.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.AdminConfigDao;
import com.ydbaobao.model.AdminConfig;

@Service
@Transactional
public class AdminConfigService {
	
	@Resource
	private AdminConfigDao adminConfigDao;
	
	public AdminConfig read() {
		return adminConfigDao.read();
	}
	
	public AdminConfig update(AdminConfig adminConfig) {
		adminConfigDao.update(adminConfig);
		return adminConfigDao.read();
	}

	public int readCostPerWeight() {
		AdminConfig config = adminConfigDao.read();
		return config.getAdminCostPerWeight();
	}
}
