package com.ydbaobao.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Brand {
	private int brandId;
	@NotNull
	private String brandName;
	private int brandCount;
	@Min(0)
	@Max(100)
	private int discount_1;
	@Min(0)
	@Max(100)
	private int discount_2;
	@Min(0)
	@Max(100)
	private int discount_3;
	@Min(0)
	@Max(100)
	private int discount_4;
	@Min(0)
	@Max(100)
	private int discount_5;
	private String brandSize;
	
	public Brand() {
		super();
	}
	
	public Brand(int brandId) {
		this.brandId = brandId;
	}
	
	public Brand(int brandId, String brandName) {
		this.brandId = brandId;
		this.brandName = brandName;
	}
	
	public Brand(int brandId, String brandName, int brandCount) {
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandCount = brandCount;
	}
	
	public Brand(int brandId, String brandName, int discount_1, int discount_2, int discount_3, int discount_4, int discount_5) {
		this(brandId, brandName, 0, discount_1, discount_2, discount_3, discount_4, discount_5, null);
	}
	
	public Brand(String brandName, int brandCount, int discount_1, int discount_2, int discount_3, int discount_4, int discount_5, String brandSize) {
		super();
		this.brandName = brandName;
		this.brandCount = brandCount;
		this.discount_1 = discount_1;
		this.discount_2 = discount_2;
		this.discount_3 = discount_3;
		this.discount_4 = discount_4;
		this.discount_5 = discount_5;
		this.brandSize = brandSize;
	}
	
	public Brand(int brandId, String brandName, int brandCount, int discount_1, int discount_2, int discount_3, int discount_4, int discount_5, String brandSize) {
		super();
		this.brandId = brandId;
		this.brandName = brandName;
		this.brandCount = brandCount;
		this.discount_1 = discount_1;
		this.discount_2 = discount_2;
		this.discount_3 = discount_3;
		this.discount_4 = discount_4;
		this.discount_5 = discount_5;
		this.brandSize = brandSize;
	}
	
	public List<Character> getFirstLetters() {
		List<Character> firstLetters = new ArrayList<Character>();
		for(char ch = 'A'; ch <= 'Z'; ch++) {
			firstLetters.add(ch);	
		}
		return firstLetters;
	}

	public int getDiscountRate(String grade) {
		if ("1".equals(grade)) {
			return this.discount_1;
		}
		if ("2".equals(grade)) {
			return this.discount_2;
		}
		if ("3".equals(grade)) {
			return this.discount_3;
		}
		if ("4".equals(grade)) {
			return this.discount_4;
		}
		if ("5".equals(grade)) {
			return this.discount_5;
		}
		return 0;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public int getBrandCount() {
		return brandCount;
	}

	public void setBrandCount(int brandCount) {
		this.brandCount = brandCount;
	}

	public int getDiscount_1() {
		return discount_1;
	}

	public void setDiscount_1(int discount_1) {
		this.discount_1 = discount_1;
	}

	public int getDiscount_2() {
		return discount_2;
	}

	public void setDiscount_2(int discount_2) {
		this.discount_2 = discount_2;
	}

	public int getDiscount_3() {
		return discount_3;
	}

	public void setDiscount_3(int discount_3) {
		this.discount_3 = discount_3;
	}

	public int getDiscount_4() {
		return discount_4;
	}

	public void setDiscount_4(int discount_4) {
		this.discount_4 = discount_4;
	}

	public int getDiscount_5() {
		return discount_5;
	}

	public void setDiscount_5(int discount_5) {
		this.discount_5 = discount_5;
	}
	
	public String getBrandSize() {
		return brandSize;
	}
	
	public void setBrandSize(String brandSize) {
		this.brandSize = brandSize;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brandCount;
		result = prime * result + brandId;
		result = prime * result
				+ ((brandName == null) ? 0 : brandName.hashCode());
		result = prime * result
				+ ((brandSize == null) ? 0 : brandSize.hashCode());
		result = prime * result + discount_1;
		result = prime * result + discount_2;
		result = prime * result + discount_3;
		result = prime * result + discount_4;
		result = prime * result + discount_5;
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
		if (brandCount != other.brandCount)
			return false;
		if (brandId != other.brandId)
			return false;
		if (brandName == null) {
			if (other.brandName != null)
				return false;
		} else if (!brandName.equals(other.brandName))
			return false;
		if (brandSize == null) {
			if (other.brandSize != null)
				return false;
		} else if (!brandSize.equals(other.brandSize))
			return false;
		if (discount_1 != other.discount_1)
			return false;
		if (discount_2 != other.discount_2)
			return false;
		if (discount_3 != other.discount_3)
			return false;
		if (discount_4 != other.discount_4)
			return false;
		if (discount_5 != other.discount_5)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Brand [brandId=" + brandId + ", brandName=" + brandName
				+ ", brandCount=" + brandCount + ", discount_1=" + discount_1
				+ ", discount_2=" + discount_2 + ", discount_3=" + discount_3
				+ ", discount_4=" + discount_4 + ", discount_5=" + discount_5
				+ ", brandSize=" + brandSize + "]";
	}

}
