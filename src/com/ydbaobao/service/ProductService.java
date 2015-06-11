package com.ydbaobao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ydbaobao.dao.BrandDao;
import com.ydbaobao.dao.ProductDao;
import com.ydbaobao.dao.StockDao;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Category;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.Stock;

@Service
public class ProductService {

	@Resource
	ProductDao productDao;
	@Resource
	StockDao stockDao;
	@Resource
	BrandDao brandDao;

	public int create(int brandId) {
		Brand brand = brandDao.readBrandByBrandId(brandId);
		Product product = new Product(brand.getBrandName(), new Category(0), brand);
		int productId = productDao.create(product);
		stockDao.createDefault(productId);
		return productId;
	}

	public Product read(int productId) {
		Product product = productDao.read(productId);
		product.setStockList(stockDao.readListByProductId(productId));
		return product;
	}

	public void updateProductImage(Product product, String imageName) {
		productDao.updateProductImage(product.getProductId(), imageName);
	}

	public void update(Product product) {
		productDao.update(product);
		updateStocks(product);
	}

	private void updateStocks(Product product) {
		List<Stock> dbStockList = stockDao.readListByProductId(product.getProductId());
		Map<Integer, Boolean> updatedStocksMap = new HashMap<Integer, Boolean>();
		for (Stock stock : dbStockList) {
			updatedStocksMap.put(stock.getStockId(), false);
		}
		for (Stock stock : product.getStockList()) {
			if (stock.getStockId() == 0) {
				if (stock.getQuantity() == 0) {
					continue;
				}
				stockDao.create(product, stock);
				continue;
			}
			updatedStocksMap.put(stock.getStockId(), true);
			stockDao.update(stock);
		}
		for (Stock stock : dbStockList) {
			if (updatedStocksMap.get(stock.getStockId()).equals(false)) {
				stockDao.delete(stock);
			}
		}
	}
}
