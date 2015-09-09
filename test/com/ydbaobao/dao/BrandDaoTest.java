package com.ydbaobao.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ydbaobao.model.Brand;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class BrandDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(BrandDaoTest.class);
	
	@Resource
	private BrandDao brandDao;
	
	@Test
	public void search() {
		List<Brand> list = brandDao.readBrandsByKeyword("%A%");
		assertNotNull(list);
		for (Brand brand : list) {
			logger.debug(brand.toString());
		}
	}
}
