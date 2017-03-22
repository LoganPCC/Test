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

import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;

/**
 * @author Zhangjr
 * Date 2007-11-29 <br>
 * Description: <br>
 * 
 */

public class WSClient {
	//正式环境下的webservice配置
	static String targetNamespace = "http://tempuri.org/"; 
	static String method = "CheckAuthorization";
	static String inParameterName = "requestXml";
	static String outParameterName = method + "Result";
	static String soapActionUri = "http://tempuri.org/CheckAuthorization";
	
//	static String targetNamespace = "http://beyondbit.com/bua/"; 
//	static String method = "Access";
//	static String inParameterName = "requestXml";
//	static String outParameterName = method + "Result";
//	static String soapActionUri = "http://beyondbit.com/bua/Access";

	public static String invoke(String url, String requestXml) throws Exception {

	
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL(url));
		
	
		OperationDesc oper = new OperationDesc();
		oper.setName(method);
		ParameterDesc param = new ParameterDesc(new QName(targetNamespace, inParameterName),
				ParameterDesc.IN, new QName("http://www.w3.org/2001/XMLSchema",
						"string"), String.class, false, false);
		param.setOmittable(true);
		oper.addParameter(param);
		
		
		oper.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		oper.setReturnClass(String.class);
		oper.setReturnQName(new QName(targetNamespace, outParameterName));
		call.setOperation(oper);
		call.setOperationName(new QName(targetNamespace, method));

		call.setUseSOAPAction(true);
		call.setSOAPActionURI(soapActionUri);

		return (String) call.invoke(new Object[] {requestXml});
	}
}
