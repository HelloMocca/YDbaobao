package com.ydbaobao.model;

public class Quantity {
	private int quantityId;
	private int itemId;
	private String size;
	private int value;
	
	public Quantity(int quantityId, int itemId, String size, int value) {
		this.quantityId = quantityId;
		this.itemId = itemId;
		this.size = size;
		this.value = value;
	}

	public int getQuantityId() {
		return quantityId;
	}

	public void setQuantityId(int quantityId) {
		this.quantityId = quantityId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemId;
		result = prime * result + quantityId;
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + value;
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
		Quantity other = (Quantity) obj;
		if (itemId != other.itemId)
			return false;
		if (quantityId != other.quantityId)
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Quantity [quantityId=" + quantityId + ", itemId=" + itemId
				+ ", size=" + size + ", value=" + value + "]";
	}
	
}
