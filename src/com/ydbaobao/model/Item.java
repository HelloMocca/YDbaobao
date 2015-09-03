package com.ydbaobao.model;

public class Item {
	private int itemId;
	private Customer customer;
	private Product product;
	private String size;
	private int quantity;
	private String itemStatus;
	private int price;
	private Payment payment;
	/**
	 * 카트 		: "I"
	 * 주문요청 	: "S"
	 * 사입처리         : "P"
	 * 취소 		: "C"
	 * 반려 		: "R"
	 */
	
	public Item() {
		
	}
	
	public Item(int itemId) {
		this(itemId, null, null, null, 0, "", 0);
	}

	public Item(int itemId, Customer customer, Product product, String size, int quantity, String itemStatus, int price) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.size = size;
		this.quantity = quantity;
		this.itemStatus = itemStatus;
		this.price = price;
	}
	
	public Item(int itemId, Customer customer, Product product, String size, int quantity, String itemStatus, int price, Payment payment) {
		this.itemId = itemId;
		this.customer = customer;
		this.product = product;
		this.size = size;
		this.quantity = quantity;
		this.itemStatus = itemStatus;
		this.price = price;
		this.payment = payment;
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
	
	public Payment getPayment() {
		return payment;
	}
	
	public void setPayment(Payment payment) {
		this.payment = payment;
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
		result = prime * result + quantity;
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		if (quantity != other.quantity)
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", customer=" + customer
				+ ", product=" + product + ", size=" + size + ", quantity="
				+ quantity + ", itemStatus=" + itemStatus + ", price=" + price
				+ "]";
	}
}
