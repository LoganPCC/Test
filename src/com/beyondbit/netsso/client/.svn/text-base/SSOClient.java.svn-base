/*
 * Copyright (c)Beyondbit Internet Software Co., Ltd. 
 * 
 * This software is the confidential and proprietary information of 
 * Beyondbit Internet Software  Co., Ltd. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it 
 * only in accordance with the terms of the license agreement you 
 * entered into with Beyondbit Internet Software Co., Ltd.
 */

package com.beyondbit.netsso.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.beyondbit.netsso.client.util.UtilString;

/**
 * @author Zhangjr
 * Date 2007-11-29 <br>
 * Description: <br>
 */

public class SSOClient implements Filter {

	private static Log logger = LogFactory.getLog(SSOClient.class.getName());

	private static final String LOGIN_URL_PARAM = "com.beyondbit.netsso.loginUrl";

	private static final String LOGOUT_URL_PARAM = "com.beyondbit.netsso.logoutUrl";

	private static final String VALIDATE_URL_PARAM = "com.beyondbit.netsso.validateUrl";

	//private static final String ACTION_URL_PARAM = "com.beyondbit.netsso.actionUrl";

	//要过滤的URL
	private static final String EXCLUDES_URL_PARAM = "com.beyondbit.netsso.excludesUrl";

	//要验证的URL
	private static final String INCLUDES_URL_PARAM = "com.beyondbit.netsso.includesUrl";

	private static final String RENEW_PARAM = "com.beyondbit.netsso.renew";

	public static final String TOMCAT_URIENCODING = "com.beyondbit.netsso.client.tomcat.uriencoding";

	public static final String PAGE_ENCODING = "com.beyondbit.netsso.client.pagencoding";


	private String loginUrl = null;
	private String logoutUrl = null;
	private String validateUrl = null;
	//    private String actionUrl = null;
	private String[] excludesUrl = null;
	private String[] includesUrl = null;
	private boolean renew = true;
	private String tomcatURIEncoding = null;
	private String pageEncoding = null;



	/**
	 * 初始化操作
	 * @throws ServletException 初始化操作失败
	 */
	public void init(FilterConfig config) throws ServletException {

		// 登录页面
		loginUrl = config.getInitParameter(LOGIN_URL_PARAM);
		// 登出页面
		logoutUrl = config.getInitParameter(LOGOUT_URL_PARAM);
		// 验证服务页（web服务）
		validateUrl = config.getInitParameter(VALIDATE_URL_PARAM);
		// 项目服务地址
		//actionUrl = config.getInitParameter(ACTION_URL_PARAM);
		// 不进行验证的URL，不包括服务地址和端口,以“,”隔开
		excludesUrl = UtilString.split(config.getInitParameter(EXCLUDES_URL_PARAM), ",");
		//要进行验证的URL，不包括服务地址和端口,以“,”隔开
		includesUrl = UtilString.split(config.getInitParameter(INCLUDES_URL_PARAM), ",");
		// 是否重新验证用户信息
		String renewStr = config.getInitParameter(RENEW_PARAM);

		tomcatURIEncoding = config.getInitParameter(TOMCAT_URIENCODING);

		if (UtilString.isEmpty(loginUrl)){
			logger.error("loginUrl parameter must be set.");
			throw new UnavailableException("loginUrl parameter must be set.");
		}

		if (UtilString.isEmpty(logoutUrl)){
			logger.error("logoutUrl parameter must be set.");
			throw new UnavailableException("logoutUrl parameter must be set.");
		}

		if (UtilString.isEmpty(validateUrl)){
			logger.error("validateUrl parameter must be set.");
			throw new UnavailableException("validateUrl parameter must be set.");
		}

		//        if (UtilString.isEmpty(actionUrl)){
		//        	logger.error("actionUrl parameter must be set.");
		//            throw new UnavailableException("actionUrl parameter must be set.");
		//        }

		if (UtilString.isEmpty(renewStr)) {
			// 默认总是重新验证用户信息
			renew = true;
		} else {
			renew = UtilString.toBoolean(renewStr);
		}

		//TOMCAT Connector 属性 URIEncoding 设置的编码
		tomcatURIEncoding = config.getInitParameter(TOMCAT_URIENCODING);

		//页面编码
		pageEncoding = config.getInitParameter(PAGE_ENCODING);

		if (UtilString.isEmpty(tomcatURIEncoding)){
			tomcatURIEncoding = "ISO8859-1";
		}

		if (UtilString.isEmpty(pageEncoding)){
			pageEncoding = "UTF-8";
		}
	}

	/**
	 * Filter processing
	 * @param request ServletRequest
	 * @param response ServletResponse
	 * @param chain FilterChain
	 * @throws IOException 异常
	 * @throws ServletException 异常
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		String ticket = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entering doFilter()");
		}

		// 当前请求的url
		String requestUrl = ((HttpServletRequest) request).getRequestURI();

		// 当前请求的url是否是不用过滤的url
		if (excludesUrl.length > 0 && isExcludeUrl(requestUrl, excludesUrl)) {
			// continue processing the request
			chain.doFilter(request, response);
			return;
		}

		//当前请求的url是否是用过滤的url
		if (includesUrl.length > 0 && !isIncludeUrl(requestUrl, includesUrl)) {
			// continue processing the request
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = ((HttpServletRequest) request).getSession();

		Receipt receipt = (Receipt) session.getAttribute(Constants.RECEIPT_KEY);

		//if our attribute's already present and valid, pass through the filter chain
		if (receipt != null) {
			if ( isReceiptAcceptable(receipt)) {
				session.setAttribute(Constants.USER_KEY, receipt.getUserUid());
				if (logger.isDebugEnabled()) {
					logger.debug("RECEIPT attribute was present and acceptable - passing  request through filter..");
				}

				chain.doFilter(request, response);
				return;
			} else {
				ticket = receipt.getTicket();
			}
		} else {
			ticket = request.getParameter(Constants.TICKET_KEY);
		}

		logger.debug("ticket: " + ticket);
		//otherwise, we need to authenticate via SSO Server
		if (ticket == null || ticket.equals("")) {
			//no ticket?  abort request processing and redirect
			redirectToSSOServer((HttpServletRequest) request, (HttpServletResponse) response);
			return;
		}


		try {
			receipt = getAuthenticatedUser((HttpServletRequest) request, ticket);

			session.setAttribute(Constants.RECEIPT_KEY, receipt);
			session.setAttribute(Constants.USER_KEY, receipt.getUserUid());
			if (logger.isDebugEnabled()) {
				logger.debug("userUid: " + receipt.getUserUid());
				logger.debug("validated ticket to get authenticated receipt [" + receipt + "], now passing request along filter chain.");
			}
		} catch (Exception e) {
			errorPage((HttpServletRequest) request, (HttpServletResponse) response);
			e.printStackTrace();
		}


		// continue processing the request
		chain.doFilter(request, response);
		if (logger.isDebugEnabled()) {
			logger.debug("returning from doFilter()");
		} 
	}

	/**
	 * 从SSO Server获取用户身份
	 * @author zhangjr
	 * Date 2007-11-29 <br>
	 * @param request HttpServletRequest
	 * @param ticket ServiceTicket
	 * @return Receipt
	 * @throws Exception 
	 */
	private Receipt getAuthenticatedUser(HttpServletRequest request, String ticket) throws Exception {
		ServiceTicketValidator ticketValidator = new ServiceTicketValidator();
		ticketValidator.setServiceTicket(ticket);
		ticketValidator.setValidateUrl(validateUrl);
		return Receipt.getReceipt(ticketValidator);
	}


	private void errorPage(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.sendRedirect("/EASSSO/errorPage.jsp");
	}

	/**
	 * 重定向到SSO Server的登录页
	 * @author zhangjr
	 * Date 2007-11-29 <br>
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws IOException 异常
	 */
	private void redirectToSSOServer(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 清空session
		request.getSession().setMaxInactiveInterval(0);
		// 组织页面跳转url
		String clientUrl = request.getScheme()
				+ "://" + request.getServerName() 
				+ ":" + request.getServerPort() 
				+ request.getRequestURI();

		//处理get请求
		//获取url后面的参数
		String getString = request.getQueryString();
		String getStringUrl = "";

		if (getString != null) {
			getString = new String(getString.getBytes(tomcatURIEncoding), pageEncoding);
			getStringUrl = "?" + URLDecoder.decode(getString, pageEncoding);
		}

		clientUrl += getStringUrl;
		clientUrl=loginUrl + "?ActionUrl=" + URLEncoder.encode(clientUrl, pageEncoding);
		System.out.println(clientUrl);
		response.sendRedirect(clientUrl);
	}

	/**
	 * 当前访问的url是否是不进行验证的URL
	 * @author Guojd
	 * Date 2007-12-4 <br>
	 * @param requestUrl 当前请求的url
	 * @param excludesUrl 不进行验证的URL数组
	 * @return
	 */
	private boolean isExcludeUrl(String requestUrl, String[] excludesUrl) {

		if (excludesUrl.length == 0) {
			return false;
		}

		for (int i = 0; i < excludesUrl.length; i++) {
			if (excludesUrl[i].equals(requestUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当前访问的url是否是进行验证的URL
	 * @author Guojd
	 * Date 2007-12-4 <br>
	 * @param requestUrl 当前请求的url
	 * @param includesUrl 进行验证的URL数组
	 * @return
	 */
	private boolean isIncludeUrl(String requestUrl, String[] includesUrl) {

		for (int i = 0; i < includesUrl.length; i++) {
			if (includesUrl[i].equals(requestUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当前Receipt是否是可接受的
	 * @author Guojd
	 * Date 2007-12-4 <br>
	 * @param receipt Receipt
	 * @return
	 */
	private boolean isReceiptAcceptable(Receipt receipt) {
		if (!renew && receipt.getUserUid() != null) {
			return true;
		} else {
			return false;
		}
	}

	public void destroy() {
	}
}
