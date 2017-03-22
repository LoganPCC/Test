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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.beyondbit.netsso.client.util.UtilRandom;
import com.beyondbit.netsso.client.util.UtilXml;

/**
 * @author Zhangjr
 * Date 2007-11-29 <br>
 * Description: <br>
 */
public class ServiceTicketValidator {

	private static Log logger = LogFactoryImpl.getLog(ServiceTicketValidator.class.getName());
	private String validateUrl = null;
	private String serviceTicket = null;
	private String userUid = null;

	public void validate() throws Exception {
		String clientAppTicket = getAppTicket();
		String requestXml = getRequestXml(clientAppTicket, serviceTicket);
		logger.debug("requestXml: " + requestXml);

		String responseXml = WSClient.invoke(validateUrl, requestXml);
		logger.debug("responseXml: " + responseXml);
		if (responseXml != null || !responseXml.equals("")) {
			String appTicket = UtilXml.getElementValue(responseXml, "AppTicket");
			String userTicket = UtilXml.getElementValue(responseXml, "UserTicket");
			String userIdentity = UtilXml.getElementValue(responseXml, "UserIdentity");

			if (!clientAppTicket.equals(appTicket)) {
				logger.error("validate error: appTicket not equal!");
				throw new AuthException("validate error");
			}

			setServiceTicket(userTicket);
			setUserUid(userIdentity);
		}
	}

	/**
	 * 生成请求requestXml
	 * @author Guojd
	 * Date 2007-12-4 <br>
	 * @param appTicket String
	 * @param serviceTicket String
	 * @return requestXml
	 */
	private String getRequestXml(String appTicket, String serviceTicket) {
		StringBuffer buf = new StringBuffer();
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buf.append("<samlp:AuthnRequest");
		buf.append(" xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"");
		buf.append(" AppTicket=\"").append(appTicket).append("\"");
		buf.append(" AppIdentity=\"\"");
		buf.append(" AppName=\"\"");
		buf.append(" Version=\"2.0\"");
		buf.append(" ProtocolBinding=\"urn:oasis:names.tc:SAML:2.0:bindings:HTTP-Redirect\"");
		buf.append(" UserTicket=\"").append(serviceTicket).append("\" />");
		return buf.toString();
	}

	private String getAppTicket() {
		return UtilRandom.getRandom(10);

	}

	public String getServiceTicket() {
		return serviceTicket;
	}
	public void setServiceTicket(String serviceTicket) {
		this.serviceTicket = serviceTicket;
	}
	public String getUserUid() {
		return userUid;
	}
	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	public String getValidateUrl() {
		return validateUrl;
	}
	public void setValidateUrl(String validateUrl) {
		this.validateUrl = validateUrl;
	}
}
