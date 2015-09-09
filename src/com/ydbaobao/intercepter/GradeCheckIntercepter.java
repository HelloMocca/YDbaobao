package com.ydbaobao.intercepter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.support.ServletRequestUtil;
import com.ydbaobao.service.CustomerService;

public class GradeCheckIntercepter extends HandlerInterceptorAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(GradeCheckIntercepter.class);

	@Resource
	private CustomerService customerService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		if (ServletRequestUtil.hasAuthorizationFromCustomer(session)) {
			return true;
		}
		if (ServletRequestUtil.hasAuthorizationFromAdmin(session)) {
			return true;
		}
		response.sendRedirect("/shop/loginForm");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}
}
