package com.ydbaobao.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.support.JSONResponseUtil;
import com.support.ServletRequestUtil;
import com.ydbaobao.model.Item;
import com.ydbaobao.service.CategoryService;
import com.ydbaobao.service.ItemService;

@Controller
@RequestMapping("/shop/carts")
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);
	@Resource
	ItemService itemService;
	@Resource
	CategoryService categoryService;
	
	/**
	 * 카트 페이지 호출 
	 * @param model
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String cartForm(Model model, HttpSession session) throws IOException {
		String customerId = ServletRequestUtil.getCustomerIdFromSession(session);
		model.addAttribute("items", itemService.readCartItems(customerId));
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		return "cart";
	}
	
	/**
	 * 카트에 아이템 추가하기 
	 * @param customerId
	 * @param productId
	 * @param size
	 * @param quantity
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Object> createItem(HttpSession session, @RequestParam int productId, @RequestParam List<String> size, @RequestParam List<Integer> quantity) throws IOException {
		String customerId = ServletRequestUtil.getCustomerIdFromSession(session);
		itemService.createItems(customerId, size, quantity, productId, Item.CART);
		return JSONResponseUtil.getJSONResponse("success", HttpStatus.OK);
	}
	
	/**
	 * 구매 수량 수정 
	 * @param model
	 * @param session
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value="/{itemId}", method = RequestMethod.PUT)
	public @ResponseBody String cartUpdate(@RequestParam int quantityId, @RequestParam int quantity, @PathVariable int itemId, Model model, HttpSession session) throws IOException {
		String customerId = ServletRequestUtil.getCustomerIdFromSession(session); 
		if (itemService.updateItemQuantity(quantityId, quantity)) {
			return "OK";
		}
		return "FAIL";
	}
	
	/**
	 * 카트에 아이템 삭제
	 * @param customerId
	 * @param itemId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteFromCart(@PathVariable int itemId, HttpSession session) throws IOException {
		String customerId = ServletRequestUtil.getCustomerIdFromSession(session); 
		itemService.deleteItem(customerId, itemId);
		return JSONResponseUtil.getJSONResponse("" + itemId, HttpStatus.OK);
	}
}
