package com.kingdee.eas;

public class SERVERConfiguration {
	
    //EAS服务器地址:生成EAS单点登录时使用
	public  static  String SERVERID="192.168.56.216:8080";

	//中间服务器地址：安全退出时将中间服务地址发给互联网认证平台
	public static  String  MIDDLESERVERID="192.168.56.216:8001";
	//public static  String  MIDDLESERVERID="192.168.56.217:8080";
	
	
	//LtpaToken.properties 文件文职
	public static String CONFIGFILE="/usr/local/tomcat/webapps/EASSSO/LtpaToken.properties";
	//public static String CONFIGFILE="F:\\LtpaToken.properties";
}
