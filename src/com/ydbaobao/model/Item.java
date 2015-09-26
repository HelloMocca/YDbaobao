package com.ydbaobao.model;

import java.util.ArrayList;
import java.util.List;

public class Item {
	private int itemId;
	private Customer customer;
	private Product product;
	private String itemStatus;
	private int price;
	private List<Quantity> quantities;
	private int orderId;
	/**
	 * 카트 		: "I"
	 * 주문요청 	: "S"
	 * 사입처리    : "A"
	 * 배송처리    : "P"
	 * 취소 		: "C"
	 * 반려 		: "R"
	 */
	public static String CART = "I";
	public static String ORDERED = "S";
	public static String ACCEPTED = "A";
	public static String SHIPMENT = "P";
	
	public Item() {
		
	}
	
	public Item(int itemId) {
		this(itemId, null, null, "", 0);
	}
	
	public Item(int itemId, List<Quantity> quantities) {
		this.itemId = itemId;
		this.quantities = quantities;
	}

	public Item(int itemId, Customer customer, Product product, String itemStatus, int price) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.itemStatus = itemStatus;
		this.price = price;
	}
	
	public Item(int itemId, Customer customer, Product product, String itemStatus, int price, int orderId) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.itemStatus = itemStatus;
		this.price = price;
		this.orderId = orderId;
	}
	
	public Item(int itemId, Customer customer, Product product, String itemStatus, int price, Quantity quantity) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.itemStatus = itemStatus;
		this.price = price;
		quantities = new ArrayList<Quantity>();
		this.quantities.add(quantity);
	}
	
	public Item(int itemId, Customer customer, Product product, String itemStatus, int price, List<Quantity> quantities) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.itemStatus = itemStatus;
		this.price = price;
		this.quantities = quantities;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	public List<Quantity> getQuantities() {
		return quantities;
	}
	
	public void setQuantities(List<Quantity> quantities) {
		this.quantities = quantities;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public String getCustomerId() {
		return customer.getCustomerId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + itemId;
		result = prime * result
				+ ((itemStatus == null) ? 0 : itemStatus.hashCode());
		result = prime * result + price;
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (itemId != other.itemId)
			return false;
		if (itemStatus == null) {
			if (other.itemStatus != null)
				return false;
		} else if (!itemStatus.equals(other.itemStatus))
			return false;
		if (price != other.price)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", customer=" + customer + ", product=" + product + ", itemStatus="
				+ itemStatus + ", price=" + price + ", quantities=" + quantities + ", orderId=" + orderId + "]";
	}
}
