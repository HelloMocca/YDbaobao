package com.ydbaobao.admincontroller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.support.JSONResponseUtil;
import com.ydbaobao.model.Brand;
import com.ydbaobao.service.BrandService;

@Controller
@RequestMapping("/admin/brands")
public class AdminBrandController {
	private static final Logger logger = LoggerFactory.getLogger(AdminBrandController.class);
	
	@Resource
	private BrandService brandService;

	/**
	 * 브랜드 관리페이지 요청
	 * @param model
	 * @return 브랜드관리페이지
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String read(Model model) {
		model.addAttribute("brands", brandService.readBrands());
		return "brandManager";
	}
	
	/**
	 * 브랜드 추가
	 * brandSize는 명시안할경우 FREE 사이즈
	 * @param brandName
	 * @param discount_1
	 * @param discount_2
	 * @param discount_3
	 * @param discount_4
	 * @param discount_5
	 * @param brandSize
	 * @return success or fail
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> create(@RequestParam String brandName, @RequestParam int discount_1,
			@RequestParam int discount_2, @RequestParam int discount_3, @RequestParam int discount_4,
			@RequestParam int discount_5, @RequestParam String brandSize) {
		if (brandSize.equals("")) brandSize = "FREE";
		Brand brand = new Brand(brandName, 0, discount_1, discount_2, discount_3, discount_4, discount_5, brandSize);
		if (brandService.createBrand(brand) < 0) {
			logger.debug("브랜드 생성 :"+brand.toString());
			return JSONResponseUtil.getJSONResponse("fail", HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse("success", HttpStatus.OK);
	}

	/**
	 * searchValue로 브랜드명 검색
	 * @param searchValue
	 * @return 브랜드명 검색결과
	 */
	@RequestMapping("/find")
	public ResponseEntity<Object> find(@RequestParam String searchValue) {
		return JSONResponseUtil.getJSONResponse(brandService.readBrandsByKeyword("%"+searchValue+"%"), HttpStatus.OK);
	}

	/**
	 * 브랜드 수정
	 * @param brand
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/{brandId}", method = RequestMethod.POST)// TODO POST -> PUT
	public ResponseEntity<Object> update(@Valid Brand brand, BindingResult result) {
		if (result.hasErrors()) {
			return JSONResponseUtil.getJSONResponse(result.getAllErrors(), HttpStatus.BAD_REQUEST);
		}
		logger.debug("브랜드 수정 :"+brand);
		brandService.updateBrand(brand);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}
	
	/**
	 * brandId 와 일치하는 브랜드 삭제
	 * @RequestMapping(value = "/{brandId}", method = RequestMethod.DELETE)
	 * @param brandId
	 * @return
	 */
	public ResponseEntity<Object> delete(@PathVariable String brandId) {
		if (brandService.deleteBrand(brandId)) {
			logger.debug("브랜드 삭제 brandId:"+brandId);
			return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
		}
		return JSONResponseUtil.getJSONResponse("fail", HttpStatus.BAD_REQUEST);
	}
}
