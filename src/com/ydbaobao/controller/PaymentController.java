package com.ydbaobao.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.support.JSONResponseUtil;
import com.support.ServletRequestUtil;
import com.ydbaobao.service.CategoryService;
import com.ydbaobao.service.PaymentService;

@Controller
@RequestMapping("/shop/payments")
public class PaymentController {
	@Resource
	private CategoryService categoryService;
	@Resource
	private PaymentService paymentService;
	
	/**
	 * Payment 페이지 요청, 세션이 없을경우 로그인페이지로 이동
	 * @param session
	 * @param model
	 * @return payment.jsp
	 * @throws IOException
	 */
	@RequestMapping(value="", method = RequestMethod.GET)
	public String readPage(HttpSession session, Model model) throws IOException {
		if(!ServletRequestUtil.hasAuthorizationFromCustomer(session)) return "loginForm";
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		return "payment";
	}
	
	/**
	 * 세션의 customerId에 해당하는 payment 정보 json 형태로 반환
	 * @param session
	 * @return 
	 * @throws IOException
	 */
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public ResponseEntity<Object> readPayments(HttpSession session) throws IOException {
		return JSONResponseUtil.getJSONResponse(paymentService.readPaymentsByCustomerId(ServletRequestUtil.getCustomerIdFromSession(session)), HttpStatus.OK);
	}
}
