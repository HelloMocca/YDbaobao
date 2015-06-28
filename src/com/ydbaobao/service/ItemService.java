package com.ydbaobao.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Payment;
import com.ydbaobao.model.Product;

@Service
@Transactional
public class ItemService {
	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
	@Resource
	private ItemDao itemDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private PaymentService paymentService;
	@Resource
	private ProductService productService;
	
	/**
	 * 아이템 생성(장바구니에 등록/ 상품화면에서 바로주문)
	 * @param customerId
	 * @param size
	 * @param quantity
	 * @param productId
	 * @param itemStatus
	 */
	public void createItems(String customerId, List<String> size, List<Integer> quantity, int productId, String itemStatus) {
		//사이즈 구분 없을 경우
		if(size.isEmpty()) {
			size.add("-");
		}
		for(int i=0; i< quantity.size(); i++){
			// 해당 사이즈가 0개일 경우
			if(quantity.get(i).equals("0"))			
				continue;
			
			// 같은 상품, 같은 사이즈, 같은상태가 존재하는지 확인
			Item item = itemDao.readItemByProductIdAndSizeAndItemStatus(productId, size.get(i), customerId, itemStatus);
			int itemId;
			if(item != null) {
				itemDao.addItemQuantity(item.getItemId(), quantity.get(i));
				itemId = item.getItemId();
			}
			else{
				itemId = itemDao.createItem(customerId, productId, size.get(i), quantity.get(i), itemStatus);
			}
			
			// 주문 가격 갱신
			if("S".equals(itemStatus)) 
				updateItemPrice(itemId);
		}
	}
	
	/**
	 * 아이템 주문 및 아이템 가격 결정
	 * @param customerId
	 * @param itemList
	 */
	public void requestItems(String customerId, int[] itemList) {
		for (int itemId : itemList) {
			// 같은조건으로 주문요청한 내역 조회
			Item item = itemDao.readItem(itemId);
			Item originItem = itemDao.readItemByProductIdAndSizeAndItemStatus(item.getProduct().getProductId(), item.getSize(), customerId, "S");
			
			// 존재할 경우 갯수 추가 및 기존 아이템 제거
			if(originItem != null) {
				itemDao.addItemQuantity(originItem.getItemId(), item.getQuantity());
				itemDao.deleteItem(itemId);
			}
			else{
				itemDao.updateItemStatus(itemId, "S");
				originItem = item;
			}
			
			// 주문 가격 갱신
			updateItemPrice(originItem.getItemId());
		}
	}

	public void updateItemPrice(int itemId) {
		Item item = itemDao.readItem(itemId);
		int price = productService.readByDiscount(item.getProduct().getProductId(), item.getCustomer()).getProductPrice();
		logger.debug(""+price);
		itemDao.updateItemPrice(item.getItemId(), item.getQuantity() * price);
	}
	
	public List<Item> readCartItems(String customerId, String itemStatus) {
		List<Item> items = itemDao.readCartItems(customerId, itemStatus);
		for (Item item : items) {
			Product product = item.getProduct();
			int discountRate = product.getBrand().getDiscountRate(item.getCustomer().getCustomerGrade());
			product.discount(discountRate);
		}
		return items;
	}
	
	public List<Item> readOrderedItems() {
		return itemDao.readOrderedItems();
	}
	
	public Item readItemByItemId(int itemId) {
		Item item = itemDao.readItem(itemId);
		Product product = item.getProduct(); 
		product.setProductPrice(productService.readByDiscount(product.getProductId(), item.getCustomer()).getProductPrice());
		return item;
	}
	
	public void deleteCartList(String customerId, int itemId) {
		if(!itemDao.readItem(itemId).getCustomer().getCustomerId().equals(customerId)){
			//TODO 아이템 고객아이디와 삭제하려는 고객아이디가 다를경우 예외처리.
		}
		itemDao.deleteItem(itemId);
	}

	public void updateItemQuantity(int itemId, int quantity) {
		itemDao.addItemQuantity(itemId, quantity);
	}

	public boolean acceptOrder(int itemId, int quantity) {
		Item item = itemDao.readItemByStatus(itemId, "S");
		if (item == null)
			return false;
		if (item.getQuantity() < quantity)
			quantity = item.getQuantity();
		int price = productService.readByDiscount(item.getProduct().getProductId(), item.getCustomer()).getProductPrice();
		paymentService.createPayment(new Payment(item.getCustomer(), "P", price * quantity));
		itemDao.addItemQuantity(itemId, -quantity);
		itemDao.updateItemPrice(item.getItemId(), item.getQuantity()*price);
		return true;
	}

	public boolean rejectOrder(int itemId) {
		Item item = itemDao.readItemByStatus(itemId, "S");
		if(item == null)
			return false;
		itemDao.updateItemStatus(itemId, "R");
		return true;
	}
}
