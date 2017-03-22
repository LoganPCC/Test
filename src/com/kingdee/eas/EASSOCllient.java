package com.kingdee.eas;

import org.apache.log4j.Logger;

import com.kingdee.eas.cp.eip.sso.ltpa.LtpaTokenManager;

public class EASSOCllient {

	private static Logger logger = Logger.getLogger("DAILY_FILE");
	
	
	public String createEasUI(String userName){
	
		LtpaTokenManager.loadConfig(SERVERConfiguration.CONFIGFILE);
		String password=null;
		while(true){
			 password = LtpaTokenManager.generate(userName, SERVERConfiguration.CONFIGFILE).toString();
			 if(password.indexOf("+")==-1){
				 break;
			 }
		}
		//���Է�����
		String url = "http://"+SERVERConfiguration.SERVERID+"/portal/index2sso.jsp?username="+userName+"&password="+password + "&redirectTo=/";
		logger.info(url);
		return url;
	}

}
