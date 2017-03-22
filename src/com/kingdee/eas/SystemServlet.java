package com.kingdee.eas;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SystemServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(SystemServlet.class);

	private static final long serialVersionUID = -833322220864312415L;

	@Override
	public void init() throws ServletException {
		String rootPath = this.getServletContext().getRealPath("/");  
		logger.info(rootPath);
		String log4jPath = this.getServletConfig().getInitParameter("oss.log4j.path");  
		//若没有指定oss.log4j.path初始参数，则使用WEB的工程目录  
		
		
		PropertyConfigurator.configure(rootPath+log4jPath);
		super.init();  
	}

}
