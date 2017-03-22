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

/**
 * @author Zhangjr
 * Date 2007-11-29 <br>
 * Description: <br>
 * 
 */


public class AuthException extends Exception {

	/**
	 * @param message
	 * @param cause
	 */
	public AuthException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
    /**
     * 构造函数
     * @param message exception message
     */
    public AuthException(String message) {
        super(message);
    }

}
