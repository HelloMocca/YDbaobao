package com.ydbaobao.dao;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Item;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class ItemDaoTest {
	private static final Logger logger = LoggerFactory.getLogger(ItemDaoTest.class);
	
	@Resource
	private ItemDao itemDao;
	
	@Test
	public void addToCartTest() {
		
	}
	
	@Test
	public void itemInsertReadDeleteTest() {
		int itemId = itemDao.createItem("Test", 1, Item.CART);
		Assert.assertNotNull(itemId);
		Item item = itemDao.readItem(itemId);
		Assert.assertNotNull(item);
		Assert.assertEquals(item.getCustomerId(), "Test");
		Assert.assertEquals(itemDao.deleteItem(itemId), true);
		Assert.assertNull(itemDao.readItem(itemId));
		logger.debug("Item Insert, Read, Delete Test Compelete!");
	}
}
