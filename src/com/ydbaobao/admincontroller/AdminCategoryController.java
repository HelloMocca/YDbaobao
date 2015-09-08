package com.ydbaobao.admincontroller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ydbaobao.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
	
	@Resource
	private CategoryService categoryService;
	
	/**
	 * 관리자 페이지에서 카테고리 추가 기능
	 * @return 성공/실패 여부만 전달하기 위해 success, fail string 전달
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public @ResponseBody String create(@RequestParam String categoryName) {
		if(categoryService.create(categoryName)) {
			return "success";
		}
		return "fail";
	}
	
	/**
	 * 관리자 페이지에서 카테고리 목록을 불러옴
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String read(Model model) {
		model.addAttribute("categories", categoryService.readWithoutUnclassifiedCategory());
		return "categoryManager";
	}

	/**
	 * 관리자 페이지에서 카테고리 이름 변경 기능
	 * @param DB에 저장된 categoryId와 변경할 categoryName
	 * @return 성공/실패 여부만 전달하기 위해 success, fail string 전달
	 */
	@RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT)
	public @ResponseBody String update(@PathVariable long categoryId, @RequestParam String categoryName) {
		System.out.println("categoryId : " + categoryId + " categoryName : " + categoryName);
		if(categoryService.update(categoryId, categoryName)) {
			return "success";
		}
		return "fail";
	}
	
	/**
	 * 관리자 페이지에서 카테고리 삭제 기능
	 * @param DB에 저장된 categoryId
	 * @return 성공/실패 여부만 전달하기 위해 success, fail string 전달
	 */
	@RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@PathVariable long categoryId) {
		if(categoryService.delete(categoryId)) {
			return "success";
		}
		return "fail";
	}
}
