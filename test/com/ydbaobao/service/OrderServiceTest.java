package com.ydbaobao.service;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.support.CommonUtil;
import com.ydbaobao.model.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class OrderServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
	
	@Resource
	private OrderService orderService;
	
	@Test
	public void readOrdersByDateTest() {
		String testDate = CommonUtil.getDate();
		Order newOrder = new Order("orderTesterId", 1200, 1200, 1200, 1200, 1200, CommonUtil.getDatetime());
		int orderId = orderService.createOrder(newOrder);
		Assert.assertNotEquals(orderId, -1);
		Assert.assertNotNull(orderService.readOrdersByDate(testDate));
		Assert.assertEquals(orderService.deleteOrder(orderId), 1);
		logger.debug("order Create-Read-Delete Test Successful");
	}
}

