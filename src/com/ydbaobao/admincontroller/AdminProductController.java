package com.ydbaobao.admincontroller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.support.CommonUtil;
import com.support.JSONResponseUtil;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Category;
import com.ydbaobao.model.Product;
import com.ydbaobao.model.SessionCustomer;
import com.ydbaobao.service.AdminConfigService;
import com.ydbaobao.service.BrandService;
import com.ydbaobao.service.CategoryService;
import com.ydbaobao.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
	private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
	
	@Resource
	private ProductService productService;
	@Resource
	private BrandService brandService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private AdminConfigService adminConfigService;
	
	/**
	 * 상품 이미지 업로드 페이지 호출
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String registProducts(Model model) {
		model.addAttribute("product", new Product()); //check is this available
		model.addAttribute("brandList", brandService.readBrands());
		return "productRegistration";
	}
	
	/**
	 * 상품 관리 페이지 호출
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String read(Model model, @RequestParam int page) {
		int selectedCategoryId = -1;
		List<Category> categories = categoryService.read();
		categories.add(0, new Category(selectedCategoryId, "전체보기", productService.count()));
		
		int totalPage = CommonUtil.countTotalPage(productService.count(), adminConfigService.read().getAdminDisplayProducts());
		
		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, totalPage));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, totalPage)).toArray());
		model.addAttribute("url", "/admin/products?page=");
		model.addAttribute("product", new Product());
		model.addAttribute("products", productService.readProductsForAdmin(page, adminConfigService.read().getAdminDisplayProducts()));
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("selectedCategoryId", selectedCategoryId);
		model.addAttribute("categories", categories);
		return "productManager";
	}
	
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	public String readByCategoryId(Model model, @RequestParam int categoryId, @RequestParam int page) {
		model.addAttribute("product", new Product());
		int productCountPerPage = adminConfigService.read().getAdminDisplayProducts();
		List<Product> products;
		int totalPage;
		if(categoryId == -1){
			products = productService.readProductsForAdmin(page, productCountPerPage);
			totalPage = CommonUtil.countTotalPage(productService.count(), productCountPerPage);
		} else{
			products = productService.readListByCategoryIdForAdmin(categoryId, page, productCountPerPage);
			totalPage = CommonUtil.countTotalPage(categoryService.readByCategoryId(categoryId).getCategoryCount(), productCountPerPage);
		}
		int selectedCategoryId = -1;
		List<Category> categories = categoryService.read();
		categories.add(0, new Category(selectedCategoryId, "전체보기", productService.count()));
		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, totalPage));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, totalPage)).toArray());
		model.addAttribute("url", "/admin/products/category?categoryId=" + categoryId + "&page=");
	
		model.addAttribute("products", products);
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("selectedCategoryId", categoryId);
		model.addAttribute("categories", categories);
		return "productManager";
	}
	
	@RequestMapping(value = "/{productId}", method = RequestMethod.GET)
	public String readByProductId(Model model, @PathVariable int productId, @RequestParam int page) {
		model.addAttribute("product", new Product());
		List<Product> products = new ArrayList<Product>();
		products.add(productService.read(productId));

		List<Category> categories = categoryService.read();
		categories.add(0, new Category(-1, "전체보기", productService.count()));

		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, page));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("url", "/admin/products/" + productId + "?page=");
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, page)).toArray());
	
		model.addAttribute("products", products);
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("categories", categories);
		return "productManager";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String readByProductName(HttpSession session, Model model, @RequestParam String productName, @RequestParam int page) {
		String terms = productService.preprocessingTerms(productName);
		model.addAttribute("product", new Product());
		List<Product> products = productService.readByProductName(terms, page, adminConfigService.read().getAdminDisplayProducts(), (SessionCustomer) session.getAttribute("sessionCustomer"));
		int count = productService.countBySearchProductName(terms);
		model.addAttribute("searchMessage", "상품명 \'"+productName+"\'에 대한 검색 결과가 "+count+" 건 있습니다.");
		int totalPage = CommonUtil.countTotalPage(count, CommonUtil.productsPerPage);
		List<Category> categories = categoryService.read();
		categories.add(0, new Category(-1, "전체보기", productService.count()));
		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, page));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("url", "/admin/products/search?productName=" + terms + "&page=");
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, totalPage)).toArray());
		model.addAttribute("products", products);
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("categories", categories);
		return "productManager";
	}
	
	/**
	 * 상품 이미지 등록(상품 등록)
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	public String imageUpload(Model model, Product product, HttpServletRequest request, @RequestParam("imageFile") MultipartFile... imageFile) {
		for(MultipartFile file:imageFile) {
			int productId = productService.create(product.getBrand().getBrandId());
			product.setProductId(productId);
			String imageName = productService.uploadImage(product, file, request);
			productService.updateProductImage(product, imageName);
		}
		model.addAttribute("brandList", brandService.readBrands());
		return "productRegistration";
	}
	
	/**
	 * 저장 된 상품 전체 삭제
	 * @return success or fail
	 */
	@RequestMapping(value = "", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteAll() {
		if(productService.deleteAll()) {
			return JSONResponseUtil.getJSONResponse("success", HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse("fail", HttpStatus.OK);
	}
	
	/**
	 * 특정 상품 삭제
	 * @param 상품Id(productId)
	 * @return success or fail
	 */
	@RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteProduct(@PathVariable int productId) {
		if(productService.delete(productId)) {
			return JSONResponseUtil.getJSONResponse("success", HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse("fail", HttpStatus.OK);
	}

	/**
	 * 특정 상품 정보 수정
	 * @param productId, productName, categoryId, brandId, productPrice, productDescription, productSize, isSoldout
	 * @return success or fail
	 */
	@RequestMapping(value = "/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Object> update(@PathVariable int productId, Product product, Category category, Brand brand){
		product.setCategory(category);
		product.setBrand(brand);
		if(productService.update(product)){
			return JSONResponseUtil.getJSONResponse("success", HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse("fail", HttpStatus.OK);
	}
}
