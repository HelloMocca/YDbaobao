package com.ydbaobao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.support.CommonUtil;
import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Payment;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.Quantity;
import com.ydbaobao.model.Customer;

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
	public void createItems(String customerId, List<String> size, List<Integer> quantities, int productId, String itemStatus) {
		int itemId;
		Quantity quantity;
		//사이즈 구분 없을 경우
		if(size.isEmpty()) {
			size.add("FREE");
		}
		Item item = itemDao.readItemByCustomerIdAndProductIdAndItemStatus(customerId, productId, itemStatus);
		if (item != null) {
			itemId = item.getItemId();
		} else {
			itemId = itemDao.createItem(customerId, productId, itemStatus);
		}
		for(int i=0; i< quantities.size(); i++){
			quantity = itemDao.readQuantityByItemIdAndSize(itemId, size.get(i));
			// 해당 사이즈가 0개일 경우
			if (quantities.get(i) == 0 && quantity != null) {
				itemDao.deleteQuantity(quantity.getQuantityId());
				continue;
			}
			if (quantity == null) {
				itemDao.createQuantity(new Quantity(0, itemId, size.get(i), quantities.get(i)));
			} else {
				itemDao.updateItemQuantity(quantity.getQuantityId(), quantity.getValue()+quantities.get(i));
			}
		}
		// 바로 구매일 경우
		if (itemStatus.equals(Item.ORDERED)) {
			updateItemPrice(itemDao.readItem(itemId));
		}
	}
	
	/**
	 * 아이템 주문 및 아이템 가격 결정
	 * @param customerId
	 * @param itemList
	 */
	public void orderItems(String customerId, int[] itemList) {
		Item item;
		Item originItem;
		List<Quantity> quantities;
		for (int itemId : itemList) {
			// 같은조건으로 주문요청한 내역 조회
			item = itemDao.readItem(itemId);
			quantities = itemDao.readQuantityByItemId(itemId);
			item.setQuantities(quantities);
			originItem = itemDao.readItemByCustomerIdAndProductIdAndItemStatus(customerId, item.getProduct().getProductId(), Item.ORDERED);		
			// 존재할 경우 갯수 추가 및 기존 아이템 제거
			if(originItem != null) {
				for (Quantity quantity : quantities) {
					itemDao.addItemQuantity(originItem.getItemId(), quantity.getSize(), quantity.getValue());
				}
				itemDao.deleteItem(itemId); // 기존의 quantity도 함께 사라짐
			}
			else{
				itemDao.updateItemStatus(itemId, Item.ORDERED);
				originItem = item;
			}		
			// 주문 가격 갱신
			updateItemPrice(originItem);
		}
	}

	/**
	 * itemId를 받아 해당 아이템의 가격 변경
	 * (구매자의 할인율 적용)
	 * @param itemId
	 */
	public void updateItemPrice(Item item) {
		int price = productService.readByDiscount(item.getProduct().getProductId(), item.getCustomer()).getProductPrice();
		int totalPrice = 0;
		List<Quantity> quantities = itemDao.readQuantityByItemId(item.getItemId());
		for (Quantity quantity : quantities) {
			totalPrice += quantity.getValue() * price;
		}
		itemDao.updateItemPrice(item.getItemId(), totalPrice);
	}
	
	public void updateItemPriceByProductId(int productId) {
		List<Item> items = itemDao.readItemsByProductId(productId);
		for (Item item : items) {
			updateItemPrice(item);
		}
	}
	
	public void updateItemPriceByCustomerId(String customerId) {
		List<Item> items = itemDao.readItemsByCustomerId(customerId);
		for (Item item : items) {
			updateItemPrice(item);
		}
	}
	
	public void updateItemPriceByBrandId(int brandId) {
		List<Item> items = itemDao.readItemsByBrandId(brandId);
		for (Item item : items) {
			updateItemPrice(item);
		}
	}
	
	/**
	 * 장바구니에 담긴 아이템 조회
	 * @param customerId
	 * @param itemList
	 */
	public List<Item> readCartItems(String customerId) {
		List<Item> items = itemDao.readCartItems(customerId);
		for (Item item : items) {
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
			Product product = item.getProduct();
			int discountRate = product.getBrand().getDiscountRate(item.getCustomer().getCustomerGrade());
			product.discount(discountRate);
		}
		return items;
	}
	
	public List<Item> readOrderedItems() {
		return itemDao.readOrderedItems();
	}
	
	public List<ItemPackage> readOrderedItemsOrderBy(String identifier) {
		if (identifier.equals("brandId")) {
			return packageByBrand(itemDao.readOrderedItemsOrderBy(identifier));			
		} else {
			return packageByCustomer(itemDao.readOrderedItemsOrderBy(identifier));
		}
	}
	
	public Item readItemByItemId(int itemId) {
		Item item = itemDao.readItem(itemId);
		Product product = item.getProduct(); 
		item.setQuantities(itemDao.readQuantityByItemId(itemId));
		product.setProductPrice(productService.readByDiscount(product.getProductId(), item.getCustomer()).getProductPrice());
		return item;
	}


	public List<Item> readOrderedItemsByPaymentId(int paymentId) {
		return itemDao.readItemsByPaymentId(paymentId);
	}
	
	public void deleteItem(String customerId, int itemId) {
		if(!itemDao.readItem(itemId).getCustomer().getCustomerId().equals(customerId)){
			//TODO 아이템 고객아이디와 삭제하려는 고객아이디가 다를경우 예외처리.
			//ControllerExceptionHandler 을 이용하여 return status로 보내줘서 처리하면 좋을 듯
			return;
		}
		itemDao.deleteItem(itemId);
	}

	public boolean updateItemQuantity(int quantityId, int quantity) {
//		if(!itemDao.readItem(itemId).getCustomer().getCustomerId().equals(customerId)){
//			//TODO 아이템 고객아이디와 수정하려는 고객아이디가 다를경우 예외처리.
//			//ControllerExceptionHandler 을 이용하여 return status로 보내줘서 처리하면 좋을 듯
//			return false;
//		}
		itemDao.updateItemQuantity(quantityId, quantity);
		return true;
	}

	public boolean acceptOrder(String[] itemIdList, String[] quantityList) {
//		Item item;
//		int price = 0;
//		int quantity;
//		int totalPrice;
//		Payment payment;
//		List<Customer> customers = new ArrayList<Customer>();
//		List<Item> items = new ArrayList<Item>();
//		//customer Listing
//		for (int i = 0; i < itemIdList.length; i++) {
//			item = itemDao.readItem(Integer.valueOf(itemIdList[i]));
//			items.add(item);
//			if(!customers.contains(item.getCustomer())) {
//				customers.add(item.getCustomer());
//			}
//		}
//		for (Customer thisCustomer : customers) {
//			int i = 0;
//			price = 0;
//			totalPrice = 0;
//			payment = paymentService.readPaymentByCustomerIdDate(thisCustomer.getCustomerId(), CommonUtil.getDate());
//			for (Item thisItem : items) {	
//				if (thisItem.getCustomer().equals(thisCustomer)) {
//					quantity = Integer.valueOf(quantityList[i]);
//					price = productService.readByDiscount(thisItem.getProduct().getProductId(), thisItem.getCustomer()).getProductPrice();
//					totalPrice += price * quantity;
//					itemDao.createItem(thisCustomer.getCustomerId(), thisItem.getProduct().getProductId(), thisItem.getSize(), quantity, "P", price * quantity, payment.getPaymentId());
//					itemDao.addItemQuantity(thisItem.getItemId(), -quantity);
//					if(thisItem.getQuantity()-quantity == 0) {
//						itemDao.deleteItem(thisItem.getItemId());
//					} else {
//						itemDao.updateItemPrice(thisItem.getItemId(), (thisItem.getQuantity()-quantity) * price);
//					}
//				}
//				i++;
//			}
//			paymentService.updatePayment(new Payment(payment.getPaymentId(), "P", payment.getAmount() +  totalPrice));
//		}
		return true;
	}

	public boolean rejectOrder(int itemId) {
//		Item item = itemDao.readItem(itemId);
//		if(item == null)
//			return false;
//		itemDao.updateItemStatus(itemId, "R");
		return true;
	}

	/**
	 * 구매자 customerId로 주문한 상품을 반환(수량 포함)
	 * @param customerId
	 * @return Quantity(수량)이 포함된 Item List 반환
	 */
	public List<Item> readOrderedItemsByCustomerId(String customerId) {
		List<Item> items = itemDao.readOrderedItemsByCustomerId(customerId);
		for (Item item : items) {
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		}
		return items;
	}
	

	public List<ItemPackage> readOrderedItemsByBrandId(int brandId) {
		return packageByBrand(itemDao.readOrderedItemsByBrandId(brandId));
	}
	
	public List<ItemPackage> packageByBrand(List<Item> items){
		List<ItemPackage> brandPacks = new ArrayList<ItemPackage>();
		HashMap<String, Integer> mapper = new HashMap<String, Integer>();
		String prevBrandName = "";
		int i = 0;
		for (Item item : items) {
			String currBrandName = item.getProduct().getBrand().getBrandName();
			if (!prevBrandName.equals(currBrandName)) {
				brandPacks.add(new ItemPackage(currBrandName));
				mapper.put(currBrandName, i); i++;
			}
			brandPacks.get(mapper.get(currBrandName)).addItem(item);
			prevBrandName = currBrandName;
			
		}
		return brandPacks;
	}
	
	public List<ItemPackage> packageByCustomer(List<Item> items){
		List<ItemPackage> customerPacks = new ArrayList<ItemPackage>();
		HashMap<String, Integer> mapper = new HashMap<String, Integer>();
		String prevCustomerId = "";
		int i = 0;
		for (Item item : items) {
			String currCustomerId = item.getCustomer().getCustomerId();
			if (!prevCustomerId.equals(currCustomerId)) {
				customerPacks.add(new ItemPackage(currCustomerId));
				mapper.put(currCustomerId, i); i++;
			}
			customerPacks.get(mapper.get(currCustomerId)).addItem(item);
			prevCustomerId = currCustomerId;
		}
		return customerPacks;
	}
	
	public class ItemPackage {
		private String key;
		private List<Item> items;

		public ItemPackage(String key) {
			this.key = key;
			this.items = new ArrayList<Item>();
		}
		
		public String getKey() {
			return key;
		}
		
		public List<Item> getItems() {
			return items;
		}
		
		public void addItem(Item item) {
			items.add(item);
		}
	}

	public List<Item> readItemsByItemIds(String[] itemIds) {
		return itemDao.readItemByItemIds(itemIds);
	}
}
