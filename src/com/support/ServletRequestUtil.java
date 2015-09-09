package com.support;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ydbaobao.model.SessionCustomer;

public class ServletRequestUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ServletRequestUtil.class);

	private ServletRequestUtil() {
	}

	public static boolean existedCustomerIdFromSession(HttpSession session, String sessionAttribute) throws IOException {
		if (session.getAttribute(sessionAttribute) == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static String getCustomerIdFromSession(HttpSession session) throws IOException {
		if (!existedCustomerIdFromSession(session, "sessionCustomer")) {
			return null;
		}
		return ((SessionCustomer) session.getAttribute("sessionCustomer")).getSessionId();
	}

	public static boolean hasAuthorizationFromCustomer(HttpSession session) throws IOException {
		if (!existedCustomerIdFromSession(session, "sessionCustomer")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public static boolean hasAuthorizationFromAdmin(HttpSession session) throws IOException {
		if (!existedCustomerIdFromSession(session, "sessionAdmin")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
