package com.ydbaobao.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.support.ImageResizeUtil;
import com.ydbaobao.dao.BrandDao;
import com.ydbaobao.dao.CategoryDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Category;
import com.ydbaobao.model.Product;

@Service
public class ProductService {
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	@Resource
	ProductDao productDao;
	@Resource
	BrandDao brandDao;
	@Resource
	CategoryDao categoryDao;

	public int create(int brandId) {
		Brand brand = brandDao.readBrandByBrandId(brandId);
		Product product = new Product(brand.getBrandName(), new Category(), brand, brand.getBrandSize());
		int productId = productDao.create(product);
		categoryDao.increaseCount(0);
		brandDao.increaseCount(brandId);
		return productId;
	}

	public Product read(int productId) {
		return productDao.read(productId);
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
		
		if(productDao.update(product)==1){
			return true;
		}
		return false;
	}
	
	public String uploadImage(Product product, MultipartFile productImage) {
		String[] imageSplitName = productImage.getOriginalFilename().split("\\.");
		String extension = imageSplitName[imageSplitName.length-1];
		String imageName = product.getProductId()+"."+extension;
		try {
			File imageFile = new File(ImageResizeUtil.savingPath + imageName);
			productImage.transferTo(imageFile);
			product.setProductImage(imageName);
			ImageResizeUtil.imageResize(imageFile.getPath(), extension);
		} catch (IllegalStateException | IOException e) {
			// TODO 예외처리 추가(giyatto)
			e.printStackTrace();
		}
		return imageName;
	}
	
	public List<Product> readListByCategoryId(int categoryId, int index, int quantity) {
		return productDao.readListByCategoryId(categoryId, index, quantity);
	}
	
	public List<Product> readProducts() {
		List<Product> productList = productDao.readProductsList();
		for(Product product:productList){
			Brand brand = product.getBrand();
			brand.setBrandName(brandDao.readBrandByBrandId(brand.getBrandId()).getBrandName());
		}
		return productList;
	}
	
	public List<Product> readByProductName(String query, int index, int quantity) {
		return productDao.readByProductName(query, index, quantity);
	}

	public List<Product> readListByCategoryId(int categoryId) {
		return productDao.readListByCategoryId(categoryId);
	}
	
	public List<Product> readByBrandName(String query, int index, int quantity) {
		return productDao.readByBrandName(query, index, quantity);
	}
	
	public int count() {
		return productDao.count();
	}
	
	public int countBySearchProductName(String terms) {
		return productDao.countBySearchProductName(terms);
	}
	
	public int countBySearchBrandName(String terms) {
		return productDao.countBySearchBrandName(terms);
	}

	public List<Product> readRange(int start, int range) {
		return productDao.readRange(start, range);
	}

	public List<Product> readUnclassifiedProducts() {
		List<Product> productList = productDao.readListByCategoryId(0);
		for(Product product:productList){
			Brand brand = product.getBrand();
			brand.setBrandName(brandDao.readBrandByBrandId(brand.getBrandId()).getBrandName());
		}
		return productList;
	}

	public List<Product> readListByBrandId(int brandId, int index, int quantity) {
		return productDao.readListByBrandId(brandId, index, quantity);
	}

	public boolean deleteAll() {
		if(productDao.deleteAll() >=1){
			categoryDao.resetCount();
			brandDao.resetCount();
			File directory = new File("/home/baobao/products/");
			for(File file : directory.listFiles()){
				file.delete();
			}
			return true;
		}
		return false;
	}

	public boolean delete(int productId) {
		Product product = productDao.read(productId);
		File file = new File("/home/baobao/products/"+product.getProductImage());
		if(productDao.delete(product) >=1){
			file.delete();
			return true;
		}
		return false;
	}

	public List<Product> readByCategoryIdAndBrandId(int categoryId, int brandId) {
		return productDao.readByCategoryIdAndBrandId(categoryId, brandId);
	}
}
