package com.ydbaobao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.ItemDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.Quantity;
import com.ydbaobao.model.Customer;

@Service
@Transactional
public class ItemService {
	@SuppressWarnings("unused")
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
		if (quantities == null) return;
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
			// 요청수량이 0개일 경우
			if (quantities.get(i) == 0) {
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
	 * 장바구니에서 아이템 주문 및 아이템 가격 결정
	 * @param customerId
	 * @param itemList
	 */
	public void orderItems(String customerId, int[] itemList) {
		Item item;
		Item originItem;
		List<Quantity> quantities;
		for (int itemId : itemList) {
			item = itemDao.readItem(itemId);
			quantities = itemDao.readQuantityByItemId(itemId);
			item.setQuantities(quantities);
			// 같은조건으로 주문요청한 내역 조회
			originItem = itemDao.readItemByCustomerIdAndProductIdAndItemStatus(customerId, item.getProduct().getProductId(), Item.ORDERED);		
			// 존재할 경우 갯수 추가 및 기존 아이템 제거
			if(originItem != null) {
				for (Quantity quantity : quantities) {
					if (!itemDao.addItemQuantity(originItem.getItemId(), quantity.getSize(), quantity.getValue())) {
						itemDao.createQuantity(new Quantity(0, originItem.getItemId(), quantity.getSize(), quantity.getValue()));
					}
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
	
	public void updateItemPriceByItemId(int itemId) {
		updateItemPrice(itemDao.readItem(itemId));
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
	
	/**
	 * identifier 기준으로 정렬된 주문되어있는 아이템 조회
	 * @param identifier
	 * @return List<Item>
	 */
	public List<ItemPackage> readOrderedItemsOrderBy(String identifier) {
		List<Item> items = itemDao.readOrderedItemsOrderBy(identifier);
		for (Item item : items) {
			item.getProduct().setProductPrice(productService.readByDiscount(item.getProduct().getProductId(), new Customer(item.getCustomerId())).getProductPrice());
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		}
		return identifier.equals("brandId") ? packageByBrand(items) : packageByCustomer(items);
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
	
	/**
	 * 주문취소
	 * @param customerId
	 * @param itemId
	 * @return 요청하는 customerId와 주문의 customerId가 다를경우 false, 쿼리결과 적용된 record가 없을경우 false 반환.
	 */
	public boolean deleteItem(String customerId, int itemId) {
		if (!itemDao.readItem(itemId).getCustomerId().equals(customerId)){
			return false;
		}
		return itemDao.deleteItem(itemId);
	}

	public boolean updateItemQuantity(int quantityId, int quantity) {
		itemDao.updateItemQuantity(quantityId, quantity);
		return true;
	}

	public boolean acceptOrder(Item item) {
		Item originItem = itemDao.readItem(item.getItemId());
		int updatedItemId = item.getItemId();
		Item sameConditionItem = itemDao.readItemByCustomerIdAndProductIdAndItemStatus(originItem.getCustomerId(), originItem.getProduct().getProductId(), Item.ACCEPTED);
		originItem.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		List<Quantity> originQuantities = originItem.getQuantities();
		List<Quantity> acceptQuantities = item.getQuantities();
		//수량 전부를 사입할 경우 acceptAllQuantity()
		if (acceptQuantities.equals(originQuantities)) {
			//사입상태인 동일 상품이 있을경우 합치고 기존아이템 삭제
			if (sameConditionItem != null) {
				this.mergeQuantity(itemDao.readQuantityByItemId(sameConditionItem.getItemId()), originQuantities);
				updatedItemId = sameConditionItem.getItemId();
				itemDao.deleteItem(originItem.getItemId());
			} else {
				//없을경우 기존것을 변경
				itemDao.updateItemStatus(item.getItemId(), Item.ACCEPTED);
			}
		} else {
			//수량 전부를 사입하지 않을경우 acceptPartOfQuantity()
			//기존의 Quantity에서 사입한 수량만큼 차감
			for (Quantity acceptQuantity : acceptQuantities) {
				for (Quantity originQuantity : originQuantities) {
					if (acceptQuantity.getSize().equals(originQuantity.getSize())) {
						itemDao.updateItemQuantity(originQuantity.getQuantityId(), originQuantity.getValue() - acceptQuantity.getValue());
					}
				}
			}
			//사입상태인 동일 상품이 있을경우 합침
			if (sameConditionItem != null) {
				this.mergeQuantity(itemDao.readQuantityByItemId(sameConditionItem.getItemId()), acceptQuantities);
				updatedItemId = sameConditionItem.getItemId();
			}
			//동일 상품이 없을경우 Item과 Quantity를 새로 만듬
			int itemId = itemDao.createItem(originItem.getCustomerId(), originItem.getProduct().getProductId(), Item.ACCEPTED);
			for (Quantity acceptQuantity : acceptQuantities) {
				itemDao.createQuantity(new Quantity(0, itemId, acceptQuantity.getSize(), acceptQuantity.getValue()));
			}
		}
		updateItemPriceByItemId(updatedItemId);
		return true;
	}

	private void mergeQuantity(List<Quantity> mergedQuantities,
			List<Quantity> mergingQuantities) {
		boolean merged;
		for (Quantity mergingQuantity : mergingQuantities) {
			merged  = false;
			for (Quantity mergedQuantity : mergedQuantities) {
				// 같은 사이즈일 경우
				if (mergedQuantity.getSize().equals(mergingQuantity.getSize())) {
					itemDao.updateItemQuantity(mergedQuantity.getQuantityId(), mergedQuantity.getValue()+mergingQuantity.getValue());
					merged = true;
				}
			}
			// 같은 사이즈가 없을경우 새로운 Quantity 생성
			if (!merged) {
				itemDao.createQuantity(new Quantity(0, mergedQuantities.get(0).getItemId(), mergingQuantity.getSize(), mergingQuantity.getValue()));
			}
		}
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
			item.getProduct().setProductPrice(productService.readByDiscount(item.getProduct().getProductId(), new Customer(customerId)).getProductPrice());
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		}
		return items;
	}
	

	public List<ItemPackage> readOrderedItemsByBrandId(int brandId) {
		List<Item> items = itemDao.readOrderedItemsByBrandId(brandId);
		for (Item item : items) {
			item.getProduct().setProductPrice(productService.readByDiscount(item.getProduct().getProductId(), new Customer(item.getCustomerId())).getProductPrice());
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		}
		return packageByBrand(items);
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
		ItemPackage currItemPack;
		int i = 0;
		for (Item item : items) {
			String currCustomerId = item.getCustomerId();
			if (!prevCustomerId.equals(currCustomerId)) {
				customerPacks.add(new ItemPackage(currCustomerId));
				mapper.put(currCustomerId, i); 
				i++;
			}
			currItemPack = customerPacks.get(mapper.get(currCustomerId));
			currItemPack.addItem(item);
			currItemPack.addPrice(item.getPrice());
			for (Quantity quantity : item.getQuantities()) {
				currItemPack.addQuantity(quantity.getValue());
			}
			prevCustomerId = currCustomerId;
		}
		return customerPacks;
	}
	
	public class ItemPackage {
		private String key;
		private List<Item> items;
		private int price;
		private int quantity;

		public ItemPackage(String key) {
			this.key = key;
			this.items = new ArrayList<Item>();
			this.price = 0;
			this.quantity = 0;
		}
		
		public String getKey() {
			return key;
		}
		
		public List<Item> getItems() {
			return items;
		}
		
		public int getPrice() {
			return price;
		}
		
		public int getQuantity() {
			return quantity;
		}
		
		public void addItem(Item item) {
			items.add(item);
		}
		
		public void addPrice(int price) {
			this.price += price;
		}
		
		public void addQuantity(int quantity) {
			this.quantity += quantity;
		}
	}

	public List<Item> readItemsByItemIds(String[] itemIds) {
		return itemDao.readItemByItemIds(itemIds);
	}
	
	public List<Item> readOrderedProductByItemIds(String[] itemIds) {
		Item currItem;
		Item newItem;
		List<Item> returnItems = new ArrayList<Item>();
		for (String itemId : itemIds) {
			currItem = itemDao.readItem(Integer.valueOf(itemId));
			newItem = new Item(currItem.getItemId(), 
					currItem.getCustomer(), 
					currItem.getProduct(), 
					currItem.getItemStatus(), 
					currItem.getPrice());
			newItem.setQuantities(itemDao.readOrderedItemQuantityByItemId(Integer.valueOf(itemId)));
			returnItems.add(newItem);
		}
		return returnItems;
	}

	public Item readItemByQuantityId(int quantityId) {
		return itemDao.readItemByQuantityId(quantityId);
	}

	public List<ItemPackage> readAcceptedItems() {
		List<Item> items = itemDao.readAcceptedItems();
		for (Item item : items) {
			item.getProduct().setProductPrice(productService.readByDiscount(item.getProduct().getProductId(), new Customer(item.getCustomerId())).getProductPrice());
			item.setQuantities(itemDao.readQuantityByItemId(item.getItemId()));
		}
		return this.packageByCustomer(items);
	}
}
