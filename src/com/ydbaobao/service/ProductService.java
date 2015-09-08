package com.ydbaobao.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.support.ImageFactoryUtil;
import com.ydbaobao.dao.BrandDao;
import com.ydbaobao.dao.CategoryDao;
import com.ydbaobao.dao.CustomerDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Category;
import com.ydbaobao.model.Customer;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.SessionCustomer;

@Service
@Transactional
public class ProductService {
	@Resource
	ProductDao productDao;
	@Resource
	BrandDao brandDao;
	@Resource
	CategoryDao categoryDao;
	@Resource
	CustomerDao customerDao;
	@Resource
	ItemService itemService;

	/**
	 * brandId에 해당하는 브랜드의 상품 생성
	 * @param brandId
	 * @return
	 */
	public int create(int brandId) {
		Brand brand = brandDao.readBrandByBrandId(brandId);
		Product product = new Product(brand.getBrandName(), new Category(), brand, brand.getBrandSize());
		int productId = productDao.create(product);
		categoryDao.increaseCount(0);
		brandDao.increaseCount(brandId);
		return productId;
	}

	/**
	 * productId와 일치하는 상품정보 반환
	 * @param productId
	 * @return
	 */
	public Product read(int productId) {
		return productDao.read(productId);
	}
	
	/**
	 * 상품아이디와 주문자정보로 상품 가격을 할인율이 적용된 가격으로 반환
	 * @param productId
	 * @param customer
	 * @return 할인율이 적용된 상품가격
	 */
	public Product readByDiscount(int productId, Customer customer) {
		Product product = productDao.read(productId);
		if (null == customer) {
			return product;
		}
		String grade = customerDao.readCustomerById(customer.getCustomerId()).getCustomerGrade();
		Brand brand = brandDao.readBrandByBrandId(product.getBrand().getBrandId());
		return product.discount(brand.getDiscountRate(grade));
	}
	
	public Product readByDiscount(int productId, SessionCustomer customer) {
		return readByDiscount(productId, new Customer(customer.getSessionId()));
	}
	
	public List<Product> readByProductName(String termsForQuery, int page, int productsPerPage, SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readByProductName(termsForQuery, (page - 1) * productsPerPage, productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}
	
	public List<Product> readByBrandName(String termsForQuery, int page, int productsPerPage, SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readByBrandName(termsForQuery, (page - 1) * productsPerPage, productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}
	
	public int countBySearchProductName(String termsForQuery) {
		return productDao.countBySearchProductName(termsForQuery);
	}
	
	public int countBySearchBrandName(String termsForQuery) {
		return productDao.countBySearchBrandName(termsForQuery);
	}

	public void updateProductImage(Product product, String imageName) {
		productDao.updateProductImage(product.getProductId(), imageName);
	}

	public Boolean update(Product product) {
		Product oldStatus = productDao.read(product.getProductId());
		long oldCategoryId = oldStatus.getCategory().getCategoryId();
		long newCategoryId = product.getCategory().getCategoryId();
		if (oldCategoryId != newCategoryId) {
			categoryDao.increaseCount(newCategoryId);
			categoryDao.decreaseCount(oldCategoryId);
		}
		long oldBrandId = oldStatus.getBrand().getBrandId();
		long newBrandId = product.getBrand().getBrandId();
		if (oldBrandId != newBrandId) {
			brandDao.increaseCount(newBrandId);
			brandDao.decreaseCount(oldBrandId);
		}
		if (null == product.getProductImage()) {
			product.setProductImage(oldStatus.getProductImage());
		}
		if (productDao.update(product) == 1) {
			if (product.getProductPrice() != oldStatus.getProductPrice() || !product.getBrand().equals(oldStatus.getBrand())) {
				itemService.updateItemPriceByProductId(product.getProductId());
			}
			return true;
		}
		return false;
	}

	public String uploadImage(Product product, MultipartFile productImage, HttpServletRequest request) {
		String[] imageSplitName = productImage.getOriginalFilename().split("\\.");
		String extension = imageSplitName[imageSplitName.length - 1];
		String imageName = product.getProductId() + "." + extension;
		try {
			String contextRoot = new HttpServletRequestWrapper(request).getRealPath("/");
			File imageFile = new File(contextRoot+ImageFactoryUtil.savingPath + imageName);
			productImage.transferTo(imageFile);
			product.setProductImage(imageName);
		} catch (IllegalStateException | IOException e) {
			// TODO 예외처리 추가(giyatto)
			e.printStackTrace();
		}
		return imageName;
	}

	private List<Product> discountAndRoundOff(SessionCustomer sessionCustomer, List<Product> products) {
		if (null == sessionCustomer) {
			return products;
		}
		String grade = customerDao.readCustomerById(sessionCustomer.getSessionId()).getCustomerGrade();
		for (Product product : products) {
			Brand brand = brandDao.readBrandByBrandId(product.getBrand().getBrandId());
			product.discount(brand.getDiscountRate(grade));
		}
		return products;
	}

	public List<Product> readRange(int page, int productsPerPage, SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readRange((page - 1) * productsPerPage, productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}

	public List<Product> readListByCategoryId(int categoryId, int page, int productsPerPage,
			SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readListByCategoryId(categoryId, (page - 1) * productsPerPage,
				productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}

	public List<Product> readListByBrandId(int brandId, int page, int productsPerPage, SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readListByBrandId(brandId, (page - 1) * productsPerPage, productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}

	public List<Product> readByCategoryIdAndBrandId(int categoryId, int brandId, int page, int productsPerPage,
			SessionCustomer sessionCustomer) {
		List<Product> products = productDao.readByCategoryIdAndBrandId(categoryId, brandId, (page - 1)
				* productsPerPage, productsPerPage);
		return discountAndRoundOff(sessionCustomer, products);
	}

	public List<Product> readProductsForAdmin(int page, int productsPerPage) {
		List<Product> products = productDao.readProductsList((page - 1) * productsPerPage, productsPerPage);
		for (Product product : products) {
			Brand brand = product.getBrand();
			brand.setBrandName(brandDao.readBrandByBrandId(brand.getBrandId()).getBrandName());
		}
		return products;
	}

	public List<Product> readListByCategoryIdForAdmin(int categoryId, int page, int productsPerPage) {
		return productDao.readListByCategoryId(categoryId, (page - 1) * productsPerPage, productsPerPage);
	}

	public int count() {
		return productDao.count();
	}

	public List<Product> readUnclassifiedProducts() {
		List<Product> productList = productDao.readListByCategoryId(0);
		for (Product product : productList) {
			Brand brand = product.getBrand();
			brand.setBrandName(brandDao.readBrandByBrandId(brand.getBrandId()).getBrandName());
		}
		return productList;
	}

	public boolean deleteAll() {
		if (productDao.deleteAll() >= 1) {
			categoryDao.resetCount();
			brandDao.resetCount();
			productDao.resetAutoIncrement();
			File directory = new File("/image/products/");
			for (File file : directory.listFiles()) {
				file.delete();
			}
			return true;
		}
		return false;
	}

	public boolean delete(int productId) {
		Product product = productDao.read(productId);
		categoryDao.decreaseCount(product.getCategory().getCategoryId());
		brandDao.decreaseCount(product.getBrand().getBrandId());
		File file = new File("/image/products/" + product.getProductImage());
		if (productDao.delete(product) >= 1) {
			file.delete();
			return true;
		}
		return false;
	}
	
	public String preprocessingTerms(String terms) {
		Pattern pt = Pattern.compile("^\\s{1,}|\\s{1,}$");
		Matcher m = pt.matcher(terms);
		String query = m.replaceAll("").replaceAll(" ", "|");
		return query;
	}
}
