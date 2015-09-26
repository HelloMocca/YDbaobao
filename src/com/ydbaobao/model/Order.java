package com.ydbaobao.model;

import java.util.List;

public class Order {
	private int orderId;
	private List<Item> items;
	private int shippingCost;
	private int extraDiscount;
	private int orderPrice;
	private int paiedPrice;
	private int recallPrice;
	private String orderDate;
	
	public Order(int shippingCost, int extraDiscount, int orderPrice, int paiedPrice, int recallPrice,
			String orderDate) {
		this(0, shippingCost, extraDiscount, orderPrice, paiedPrice, recallPrice, orderDate);
	}
	
	public Order(int orderId, int shippingCost, int extraDiscount, int orderPrice, int paiedPrice, int recallPrice,
			String orderDate) {
		this.orderId = orderId;
		this.shippingCost = shippingCost;
		this.extraDiscount = extraDiscount;
		this.orderPrice = orderPrice;
		this.paiedPrice = paiedPrice;
		this.recallPrice = recallPrice;
		this.orderDate = orderDate;
	}
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public int getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(int shippingCost) {
		this.shippingCost = shippingCost;
	}
	public int getExtraDiscount() {
		return extraDiscount;
	}
	public void setExtraDiscount(int extraDiscount) {
		this.extraDiscount = extraDiscount;
	}
	public int getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getPaiedPrice() {
		return paiedPrice;
	}
	public void setPaiedPrice(int paiedPrice) {
		this.paiedPrice = paiedPrice;
	}
	public int getRecallPrice() {
		return recallPrice;
	}
	public void setRecallPrice(int recallPrice) {
		this.recallPrice = recallPrice;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + extraDiscount;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result
				+ ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + orderId;
		result = prime * result + orderPrice;
		result = prime * result + paiedPrice;
		result = prime * result + recallPrice;
		result = prime * result + shippingCost;
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
		Order other = (Order) obj;
		if (extraDiscount != other.extraDiscount)
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (orderId != other.orderId)
			return false;
		if (orderPrice != other.orderPrice)
			return false;
		if (paiedPrice != other.paiedPrice)
			return false;
		if (recallPrice != other.recallPrice)
			return false;
		if (shippingCost != other.shippingCost)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", items=" + items
				+ ", shippingCost=" + shippingCost + ", extraDiscount="
				+ extraDiscount + ", orderPrice=" + orderPrice
				+ ", paiedPrice=" + paiedPrice + ", recallPrice=" + recallPrice
				+ ", orderDate=" + orderDate + "]";
	}
}
