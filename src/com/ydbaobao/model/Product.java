package com.ydbaobao.model;

public class Product {
	private int productId;
	private String productName;
	private Category category;
	private Brand brand;
	private int productPrice;
	private String productImage;
	private String productDescription;
	private long productCreateDate;
	private long productUpdateDate;
	private String productSize;
	private int isSoldout;
	
	public Product() {
		
	}

	public Product(int productId) {
		this(productId, null, new Category(), new Brand(), 0, null, null, 0, 0, null, 0);
	}

	public Product(int productId, String productName, int productPrice, String productImage, String productSize,
			int isSoldout, Brand brand) {
		this(productId, productName, new Category(), brand, productPrice, productImage, null, 0, 0, productSize,
				isSoldout);
	}

	public Product(String productName, Category category, Brand brand, String productSize) {
		super();
		this.productName = productName;
		this.category = category;
		this.brand = brand;
		this.productSize = productSize;
	}

	public Product(int productId, String productName, Category category, Brand brand, int productPrice,
			String productDecription, String productSize) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.category = category;
		this.brand = brand;
		this.productPrice = productPrice;
		this.productDescription = productDecription;
		this.productSize = productSize;
	}

	public Product(int productId, String productName, Category category, Brand brand, int productPrice,
			String productImage, String productDescription, long productCreateDate, long productUpdateDate,
			String productSize, int isSoldout) {
		this.productId = productId;
		this.productName = productName;
		this.category = category;
		this.brand = brand;
		this.productPrice = productPrice;
		this.productImage = productImage;
		this.productDescription = productDescription;
		this.productCreateDate = productCreateDate;
		this.productUpdateDate = productUpdateDate;
		this.productSize = productSize;
		this.isSoldout = isSoldout;
	}

	public Product discount(int discountRate) {
		int rate = 100 - discountRate;
		this.productPrice = roundingOff((double) this.productPrice * rate / 100);
		return this;
	}

	private int roundingOff(double price) {
		return (int) Math.round(price / 100.0) * 100;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public long getProductCreateDate() {
		return productCreateDate;
	}

	public void setProductCreateDate(long productCreateDate) {
		this.productCreateDate = productCreateDate;
	}

	public long getProductUpdateDate() {
		return productUpdateDate;
	}

	public void setProductUpdateDate(long productUpdateDate) {
		this.productUpdateDate = productUpdateDate;
	}

	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	public int getIsSoldout() {
		return isSoldout;
	}

	public void setIsSoldout(int isSoldout) {
		this.isSoldout = isSoldout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + isSoldout;
		result = prime * result + (int) (productCreateDate ^ (productCreateDate >>> 32));
		result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
		result = prime * result + productId;
		result = prime * result + ((productImage == null) ? 0 : productImage.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + productPrice;
		result = prime * result + ((productSize == null) ? 0 : productSize.hashCode());
		result = prime * result + (int) (productUpdateDate ^ (productUpdateDate >>> 32));
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
		Product other = (Product) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (isSoldout != other.isSoldout)
			return false;
		if (productCreateDate != other.productCreateDate)
			return false;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		if (productId != other.productId)
			return false;
		if (productImage == null) {
			if (other.productImage != null)
				return false;
		} else if (!productImage.equals(other.productImage))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (productPrice != other.productPrice)
			return false;
		if (productSize == null) {
			if (other.productSize != null)
				return false;
		} else if (!productSize.equals(other.productSize))
			return false;
		if (productUpdateDate != other.productUpdateDate)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", category=" + category
				+ ", brand=" + brand + ", productPrice=" + productPrice + ", productImage=" + productImage
				+ ", productDescription=" + productDescription + ", productCreateDate=" + productCreateDate
				+ ", productUpdateDate=" + productUpdateDate + ", productSize=" + productSize + ", isSoldout="
				+ isSoldout + "]";
	}

}
