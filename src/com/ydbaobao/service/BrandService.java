package com.ydbaobao.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydbaobao.dao.BrandDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.model.Brand;

@Service
@Transactional
public class BrandService {
	@Resource
	private BrandDao brandDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private ItemService itemService;
	
	public int createBrand(Brand brand) {
		if(brandDao.readBrandByBrandName(brand.getBrandName()) != null) {
			return -1;
		}
		return brandDao.createBrand(brand);
	}

	public List<Brand> readBrands() {
		return brandDao.readBrands();
	}
	
	public Brand readBrandByBrandId(int brandId) {
		return brandDao.readBrandByBrandId(brandId);
	}	
	
	public List<Brand> readBrandsByCategoryId(int categoryId) {
		List<Brand> brands = brandDao.readBrandsByCategoryId(categoryId);
		// 브랜드별 상품 개수 다시 계산.
		int count=0;
		for(Brand brand:brands){
			count = productDao.countProductByBrandIdAndCategoryId(categoryId, brand.getBrandId());
			brand.setBrandCount(count);			
		}
		return brands;
	}
	
	public List<Brand> readOrderedBrandList() {
		return brandDao.readOrderedBrandList();
	}
	
	public List<Brand> readBrandsByKeyword(String keyword) {
		return brandDao.readBrandsByKeyword(keyword);
	}
	
	/**
	 * 브랜드 정보 변경
	 * 등급 할인율 변경시 현재 주문 진행중인 모든 아이템에 새로운 가격 적용
	 * @param brand
	 */
	public void updateBrand(Brand brand) {
		Brand prevBrand = brandDao.readBrandByBrandId(brand.getBrandId());
		if(brandDao.readBrandByBrandName(brand.getBrandName()) != null) {
			// TODO 브랜드명 중복 예외처리
		}
		brandDao.updateBrand(brand);
		if (prevBrand.getDiscount_1() != brand.getDiscount_1() || 
			prevBrand.getDiscount_2() != brand.getDiscount_2() ||
			prevBrand.getDiscount_3() != brand.getDiscount_3() ||
			prevBrand.getDiscount_4() != brand.getDiscount_4() ||
			prevBrand.getDiscount_5() != brand.getDiscount_5()) {
			itemService.updateItemPriceByBrandId(brand.getBrandId());
		}
	}

	public boolean deleteBrand(String brandId) {
		if(brandDao.deleteBrand(brandId) == 1) return true;
		return false;
	}
}
