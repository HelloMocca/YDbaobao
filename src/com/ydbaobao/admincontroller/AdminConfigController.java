package com.ydbaobao.admincontroller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.support.CommonUtil;
import com.ydbaobao.model.AdminConfig;
import com.ydbaobao.service.AdminConfigService;

@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {
	private static final Logger logger = LoggerFactory.getLogger(AdminConfigController.class);

	@Resource
	private AdminConfigService adminConfigService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String read(Model model) {
		model.addAttribute("adminConfig", adminConfigService.read());
		return "config";
	}
	
	@RequestMapping(value = "", method = {RequestMethod.POST})
	public String update(Model model, AdminConfig adminConfig) {
		CommonUtil.productsPerPage = adminConfig.getAdminDisplayProducts();
		logger.debug(adminConfig.getAdminPassword());
		if ("".equals(adminConfig.getAdminPassword())) {
			adminConfig.setAdminPassword(adminConfigService.read().getAdminPassword());
		}
		adminConfigService.update(adminConfig);
		model.addAttribute("message", "설정이 변경되었습니다.");
		return read(model);
	}
}
