package com.ydbaobao.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.OrderDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Item;

@Service
@Transactional
public class ItemService {
	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
	@Resource
	private ItemDao itemDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private ProductDao productDao;
	
	public void createItems(String customerId, String size, String quantity, int productId) {
		String[] sizeArray = size.split("-");
		String[] quantityArray = quantity.split("-");
		for(int i=0; i< quantityArray.length; i++){
			if(quantityArray[i].equals("0")){
				continue;
			}
			if(itemDao.isItemByProductIdAndSize(productId, sizeArray[i])){
				itemDao.updateItem(itemDao.readItemByProductIdAndSize(productId, sizeArray[i]).getItemId(), quantityArray[i]);
			}
			else{
				itemDao.createItem(customerId, productId, sizeArray[i], Integer.parseInt(quantityArray[i]));
			}
		}
	}

	public List<Item> readOrderedItems(String customerId) {
		return itemDao.readOrderedItems(customerId);
	}

	public void deleteCartList(String customerId, int itemId) {
		if(!itemDao.readItemByItemId(itemId).getCustomer().getCustomerId().equals(customerId)){
			//TODO 아이템 고객아이디와 삭제하려는 고객아이디가 다를경우 예외처리.
		}
		itemDao.deleteCartList(itemId);
	}

	public void orderDirect(String customerId, int productId, String size, int quantity) {
//		itemDao.orderDirect(customerId, productId, orderDao.createOrder(customerId), size, quantity);
	}

	public List<Item> readCartItems(String customerId) {
		return itemDao.readCartItems(customerId);
	}
}
