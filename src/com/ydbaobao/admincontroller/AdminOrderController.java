package com.ydbaobao.admincontroller;

import javax.annotation.Resource;

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

import com.support.JSONResponseUtil;
import com.ydbaobao.service.ItemService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
	private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
	
	@Resource
	private ItemService itemService;

	/**
	 * 주문된 브랜드의 리스트 출력 페이지 요청
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/brands", method = RequestMethod.GET)
	public String manageOrderByBrands(Model model) {
		model.addAttribute("brandPacks", itemService.readOrderedItemsOrderBy("brandId"));
		return "orderManagerBrandList";
	}
	
	/**
	 * brandId에 해당하는 브랜드의 주문 목록 페이지 요청
	 * @param brandId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/brand/{brandId}", method = RequestMethod.GET)
	public String manageOrderByBrandId(@PathVariable int brandId, Model model) {
		model.addAttribute("brandPacks", itemService.readOrderedItemsByBrandId(brandId));
		return "orderManagerByBrand";
	}
	
	/**
	 * 모든 유저에 대한 주문리스트 및 페이지 요청
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String manageOrderByCustomers(Model model) {
		model.addAttribute("customerPacks", itemService.readOrderedItemsOrderBy("customerId"));
		return "orderManagerByCustomer";
	}
	
	/**
	 * customerId를 받아 해당 유저의 주문목록 리스트 및 페이지 요청
	 * @param customerId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
	public String manageCustomerOrder(@PathVariable String customerId, Model model) {
		model.addAttribute("items", itemService.readOrderedItemsByCustomerId(customerId));
		return "orderManager";
	}

	/**
	 * 관지자 화면에서 주문 내역에 대한 상태 변경(승인, 반려)
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value = "/accept", method = RequestMethod.POST)
	public ResponseEntity<Object> acceptOrder(@RequestParam int[] itemList, @RequestParam int[] quantityList) {
		itemService.acceptOrder(itemList, quantityList);
		return JSONResponseUtil.getJSONResponse("주문승인", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/reject/{itemId}", method = RequestMethod.POST)
	public ResponseEntity<Object> rejectOrder(@PathVariable int itemId) {
		if(!itemService.rejectOrder(itemId))
			return JSONResponseUtil.getJSONResponse("유효하지 않은 주문입니다.", HttpStatus.OK);
		return JSONResponseUtil.getJSONResponse("주문반려", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ordersheet")
	public String requestOrdersheet() {
		return "ordersheet";
	}
	
}
