package com.ydbaobao.admincontroller;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.support.Message;
import com.ydbaobao.model.Item;
import com.ydbaobao.model.Order;
import com.ydbaobao.service.BrandService;
import com.ydbaobao.service.ItemService;
import com.ydbaobao.service.OrderService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AdminOrderController.class);
	
	@Resource
	private ItemService itemService;
	
	@Resource
	private BrandService brandService;
	
	@Resource
	private OrderService orderService;

	/**
	 * 주문된 브랜드의 리스트 출력 페이지 요청
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/brands", method = RequestMethod.GET)
	public String manageOrderByBrands(Model model) {
		model.addAttribute("brandList", brandService.readOrderedBrandList());
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
	 * 주문에 대한 사입처리
	 * @param itemList [처리(변동)될 Item의 Id 배열]
	 * @param sizeList [각 Item에서 변동될 Size 배열]
	 * @param quantityList [각 Item에서 변동될 수량 배열]
	 * @return 성공처리여부
	 */
	@RequestMapping(value = "/accept", method = RequestMethod.POST)
	public @ResponseBody String acceptOrder(@RequestParam String itemList) {
		Type collectionType = new TypeToken<List<Item>>(){}.getType();
		List<Item> items = new Gson().fromJson(itemList, collectionType);
		System.out.println(items);
		for (Item item : items) {
			if (!itemService.acceptOrder(item)) {
				return Message.FAIL;
			}
		}
		return Message.OK;
	}
	
	/**
	 * 사입된 주문 관리
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/accepted")
	public String acceptedOrder(Model model) {
		model.addAttribute("customerPacks", itemService.readAcceptedItems());
		return "acceptedOrderManager";
	}
	
	/**
	 * 주문 배송처리
	 * @return
	 */
	@RequestMapping(value = "/shipping", method = RequestMethod.POST)
	public @ResponseBody String shippingOrder(@RequestParam String order) {
		Type collectionType = new TypeToken<Order>(){}.getType();
		Order newOrder = new Gson().fromJson(order, collectionType);
		return (orderService.createOrder(newOrder)) ? Message.OK : Message.FAIL;
	}
	
	/**
	 * 배송처리된 주문관리
	 */
	public String shippedOrder(Model model) {
		//model.addAttribute("items", itemService.readShippedItemsByDate());
		return "shippedOrderManager";
	}
	
	/**
	 * 주문에 대해 반려처리
	 * @param itemId
	 * @return
	 */
	@RequestMapping(value = "/reject/{itemId}", method = RequestMethod.POST)
	public @ResponseBody String rejectOrder(@PathVariable int itemId) {
		if(!itemService.rejectOrder(itemId))
			return "유효하지 않은 주문입니다.";
		return "success";
	}
	
	/**
	 *  해당 Item들에 대하여 주문서를 출력
	 * @param itemIdList
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ordersheet/{itemIdList}")
	public String requestOrdersheet(@PathVariable String itemIdList, Model model) {
		List<Item> items = itemService.readOrderedProductByItemIds(itemIdList.split(","));
		model.addAttribute("itemList", items);
		return "ordersheet";
	}
	
}
