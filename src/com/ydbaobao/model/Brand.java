package com.ydbaobao.model;

public class Brand {
	private long brandId;
	private String brandName;
	
	public Brand() {
	}

	public Brand(long brandId, String brandName) {
		this.brandId = brandId;
		this.brandName = brandName;
	}

	public long getBrandId() {
		return brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (brandId ^ (brandId >>> 32));
		result = prime * result
				+ ((brandName == null) ? 0 : brandName.hashCode());
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
		Brand other = (Brand) obj;
		if (brandId != other.brandId)
			return false;
		if (brandName == null) {
			if (other.brandName != null)
				return false;
		} else if (!brandName.equals(other.brandName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Brand [brandId=" + brandId + ", brandName=" + brandName + "]";
	}
}
