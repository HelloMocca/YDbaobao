package com.ydbaobao.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.support.CommonUtil;
import com.support.JSONResponseUtil;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.SessionCustomer;
import com.ydbaobao.service.BrandService;
import com.ydbaobao.service.CategoryService;
import com.ydbaobao.service.ProductService;

@Controller
@RequestMapping("/shop/brands")
public class BrandController {
	@Resource
	private BrandService brandService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private ProductService productService;
	
	/**
	 * index.jsp에서 브랜드 메뉴 검색하는 곳에 사용
	 * @param 검색에 사용될 브랜드 이름의 첫글자(A~Z)
	 * @return 검색된 결과를 JSON 데이타로 전달
	 */
	@RequestMapping(value = "/{firstLetter}", method = RequestMethod.GET)
	public ResponseEntity<Object> readByFirstLetter(@PathVariable String firstLetter) {
		if (firstLetter.equals("all")) firstLetter = "";
		return JSONResponseUtil.getJSONResponse(brandService.readBrandsByKeyword(firstLetter+"%"), HttpStatus.OK);
	}
	
	/**
	 * 브랜드별 전체 상품
	 * @param 검색에 사용될 브랜드 Id, page 숫자
	 */
	@RequestMapping(value="/{brandId}/products", method=RequestMethod.GET)
	public String readByBrandId(HttpSession session, @RequestParam int page, Model model, @PathVariable int brandId) {
		Brand brand = brandService.readBrandByBrandId(brandId);
		int totalPage = CommonUtil.countTotalPage(brand.getBrandCount(), CommonUtil.productsPerPage);
		
		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, totalPage));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, totalPage)).toArray());
		model.addAttribute("url", "/shop/brands/" + brandId + "/products/?page=");
		model.addAttribute("products", productService.readListByBrandId(brandId, page, CommonUtil.productsPerPage, (SessionCustomer) session.getAttribute("sessionCustomer")));
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("brand", brand);
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		model.addAttribute("firstLetterList", new Brand().getFirstLetters());

		return "products";
	}
}
