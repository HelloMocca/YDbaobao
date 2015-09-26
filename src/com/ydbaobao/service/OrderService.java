package com.ydbaobao.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.OrderDao;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Order;

@Service
@Transactional
public class OrderService {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	@Resource
	private OrderDao orderDao;
	@Resource
	private ItemDao itemDao;
	
	public boolean createOrder(Order order) {
		int orderId = orderDao.createOrder(order);
		List<Item> items = order.getItems();
		for (Item item : items) {
			if (!itemDao.updateItemToShipmentStatus(item.getItemId(), orderId)) return false;
		}
		return true;
	}

	public List<Order> readOrdersByDate(String date) {
		
		return null;
	}
}
