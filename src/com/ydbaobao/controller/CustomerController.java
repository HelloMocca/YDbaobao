package com.ydbaobao.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.support.CommonUtil;
import com.support.ServletRequestUtil;
import com.ydbaobao.exception.ExceptionForMessage;
import com.ydbaobao.exception.JoinValidationException;
import com.ydbaobao.model.Brand;
import com.ydbaobao.model.Customer;
import com.ydbaobao.model.SessionCustomer;
import com.ydbaobao.service.AdminConfigService;
import com.ydbaobao.service.AdminIndexImageService;
import com.ydbaobao.service.BrandService;
import com.ydbaobao.service.CategoryService;
import com.ydbaobao.service.CustomerService;
import com.ydbaobao.service.ProductService;

@Controller
@RequestMapping("/shop/customers")
public class CustomerController {
	@Resource
	private CustomerService customerService;
	@Resource
	private CategoryService categoryService;
	@Resource
	private BrandService brandService;
	@Resource
	private ProductService productService;
	@Resource
	private AdminConfigService adminConfigService;
	@Resource
	private AdminIndexImageService adminIndexImageService;
	
	@RequestMapping(value = "/updateForm", method = RequestMethod.GET)
	public String updateForm(Model model, HttpSession session) throws IOException {
		String customerId = ServletRequestUtil.getCustomerIdFromSession(session);
		model.addAttribute("isUpdate", true);
		model.addAttribute("customer", customerService.readCustomer(customerId));
		return "form";
	}
	
	//TODO form을 put으로 넘기는 방법 찾기!
	@RequestMapping(value ="/update", method = RequestMethod.POST)
	public String update(@Valid Customer customer, BindingResult result, @RequestParam String customerAgainPassword, Model model, HttpSession session) throws ExceptionForMessage, IOException{
		if(result.hasErrors()) {
			throw new JoinValidationException(CommonUtil.extractValidationMessages(result));
        }
		if(!customer.getCustomerPassword().equals(customerAgainPassword)) {
			String customerId = ServletRequestUtil.getCustomerIdFromSession(session);
			model.addAttribute("isUpdate", true);
			model.addAttribute("customer", customerService.readCustomer(customerId));
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "form";
		}
		customerService.update(customer);
		// index로 넘기기 전에 모델에 상품 정보, 카테고리 등 넣기.
		
		int totalPage = CommonUtil.countTotalPage(productService.count(), CommonUtil.productsPerPage);

		model.addAttribute("prev", CommonUtil.prevBlock(1));
		model.addAttribute("next", CommonUtil.nextBlock(1, totalPage));
		model.addAttribute("selectedIndex", 1);
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(1), CommonUtil.endPage(1, totalPage))
				.toArray());
		model.addAttribute("url", "/shop/index?page=");
		model.addAttribute("products", productService.readRange(1, adminConfigService.read().getAdminDisplayProducts(),
				(SessionCustomer) session.getAttribute("sessionCustomer")));
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("firstLetterList", new Brand().getFirstLetters());
		model.addAttribute("indexImages", new Gson().toJson(adminIndexImageService.readIndexImages()));
		model.addAttribute("isHome", "home");
		return "indexmain";
	}
}
