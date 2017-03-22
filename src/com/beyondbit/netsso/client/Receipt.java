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

import java.io.Serializable;

/**
 * @author Zhangjr
 * Date 2007-11-29 <br>
 * Description: <br>
 * 
 */

public class Receipt implements Serializable {
	private String userUid = null;
	private String ticket = null;
	
	public static Receipt getReceipt(ServiceTicketValidator stv) throws Exception {
		
		stv.validate();

		Receipt receipt = new Receipt();
		receipt.setTicket(stv.getServiceTicket());
		receipt.setUserUid(stv.getUserUid());
		
		return receipt;
	}

	public String getUserUid() {
		return userUid;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
}
