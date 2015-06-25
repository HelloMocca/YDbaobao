package com.ydbaobao.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.CustomerDao;
import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.OrderDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Customer;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Order;
import com.ydbaobao.model.Product;

@Service
@Transactional
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Resource
	OrderDao orderDao;
	@Resource
	ItemDao itemDao;
	@Resource
	ProductDao productDao;
	@Resource
	ProductService productService;
	@Resource
	CustomerDao customerDao;
	
	public List<Order> readOrders() {
		return orderDao.readOrders();
	}
	
	public List<Order> readOrdersByCustomerId(String customerId) {
		List<Order> orders = orderDao.readOrdersByCustomerId(customerId);
		List<Item> items = itemDao.readOrderedItems(customerId);
		for (Item item : items) {
			Product product = item.getProduct();
			product.setProductPrice(productService.readByDiscount(product, item.getCustomer()).getProductPrice());
		}
		return repackOrders(orders, items);
	}
	
	private List<Order> repackOrders(List<Order> orders, List<Item> items) {
		Map<String,Order> mapOrders = new HashMap<String,Order>();
		for (Order order : orders) {
			mapOrders.put(""+order.getOrderId(),order);
		}
		
		for (Item item : items) {
			mapOrders.get(""+item.getOrder().getOrderId()).addItem(item);
		}
		
		List<Order> list = new ArrayList<Order>(mapOrders.values());
		Collections.reverse(list);
		return list;
	}

	public Order readOrder(int orderId) {
		Order order = orderDao.readOrder(orderId);
		Customer customer = customerDao.readCustomerById(order.getCustomer().getCustomerId());
		order.setCustomer(new Customer(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerGrade()));
		List<Item> items = itemDao.readItemsByOrderId(orderId);
		order.setItems(items);
		return order;
	}
	
	public Item readItemByItemId(int itemId) {
		Item item = itemDao.readItemByItemId(itemId);
		Product product = item.getProduct(); 
		product.setProductPrice(productService.readByDiscount(product, item.getCustomer()).getProductPrice());
		return item;
	}

	public void createOrder(String customerId, int[] itemList) {
		int totalPrice = 0;
		for(int i=0; i<itemList.length; i++) {
			Item item = itemDao.readItemByItemId(itemList[i]);
			Product product = productDao.read(item.getProduct().getProductId());
			int price = productService.readByDiscount(product, item.getCustomer()).getProductPrice();
			totalPrice += price * item.getQuantity();
		}
		int orderId = orderDao.createOrder(customerId, totalPrice);
		itemDao.orderItems(orderId, itemList);
	}

	public void updateOrder(int orderId, String orderStatus) {
		logger.debug("orderId: {}, orderStatus: {}", orderId, orderStatus);
		orderDao.updateOrder(orderId, orderStatus);
	}
}
