package com.ydbaobao.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.google.gson.Gson;
import com.support.CommonUtil;
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
public class HomeController {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Resource
	private CategoryService categoryService;
	@Resource
	private CustomerService customerService;
	@Resource
	private BrandService brandService;
	@Resource
	private ProductService productService;
	@Resource
	private AdminConfigService adminConfigService;
	@Resource
	private AdminIndexImageService adminIndexImageService;

	@RequestMapping(value="/shop", method = RequestMethod.GET)
	public String home(Model model, WebRequest req, HttpSession session) {
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

	@RequestMapping(value = "/shop/index", method = RequestMethod.GET)
	public String homeWithPage(Model model, HttpSession session, @RequestParam int page) {
		int totalPage = CommonUtil.countTotalPage(productService.count(), CommonUtil.productsPerPage);

		model.addAttribute("prev", CommonUtil.prevBlock(page));
		model.addAttribute("next", CommonUtil.nextBlock(page, totalPage));
		model.addAttribute("selectedIndex", page);
		model.addAttribute("range", CommonUtil.range(CommonUtil.startPage(page), CommonUtil.endPage(page, totalPage)).toArray());
		model.addAttribute("url", "/shop/index?page=");
		model.addAttribute("products", productService.readRange(page, adminConfigService.read()
				.getAdminDisplayProducts(), (SessionCustomer) session.getAttribute("sessionCustomer")));
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		model.addAttribute("brands", brandService.readBrands());
		model.addAttribute("firstLetterList", new Brand().getFirstLetters());

		StringBuilder imgPath = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			String filePath = "index_0" + i + ".jpg";
			File f = new File("/image/index/" + filePath);
			if (f.isFile()) {
				imgPath.append(",/image/index/" + filePath);
			}
		}
		model.addAttribute("imgPath", imgPath);
		return "indexmain";
	}

	@RequestMapping(value = "/shop/loginForm", method = RequestMethod.GET)
	public String loginView(Model model) {
		model.addAttribute("customer", new Customer());
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		return "login";
	}

	@RequestMapping(value = "/shop/login", method = RequestMethod.POST)
	protected String login(@RequestParam String customerId, @RequestParam String customerPassword, HttpSession session, Model model) throws ExceptionForMessage {
		SessionCustomer sessionCustomer = (customerService.login(customerId, customerPassword)).createSessionCustomer();
		session.setAttribute("sessionCustomer", sessionCustomer);
		return "redirect:/shop";
	}

	@RequestMapping(value = "/shop/joinForm", method = RequestMethod.GET)
	public String form(Model model) {
		model.addAttribute("customer", new Customer());
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		return "form";
	}

	@RequestMapping(value = "/shop/join", method = RequestMethod.POST)
	public String create(@Valid Customer customer, BindingResult result, @RequestParam String customerAgainPassword,
			Model model) throws ExceptionForMessage {
		if (result.hasErrors()) {
			throw new JoinValidationException(CommonUtil.extractValidationMessages(result));
		}
		if (!customer.getCustomerPassword().equals(customerAgainPassword)) {
			model.addAttribute("customer", new Customer());
			model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
			return "form";
		}
		customerService.join(customer);
		return "redirect:/shop/loginForm";
	}

	@RequestMapping("/shop/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/shop/loginForm";
	}
}
