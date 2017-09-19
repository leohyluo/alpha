package com.alpha.self.diagnosis.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alpha.commons.core.framework.SpringContextHolder;
import com.alpha.self.diagnosis.processor.BasicAnswerProcessorAdaptor;


@WebListener
public class DiagnosisContextListener implements ServletContextListener {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		
	}

	/*@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("DiagnosisContextListener contextInitialized starting...");
		//获取spring上下文，初始化全局SpringContextHolder
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SpringContextHolder.setApplicationContext(applicationContext);
		//初始化基础答案适配器
		BasicAnswerProcessorAdaptor.initial();
		
		logger.info("DiagnosisContextListener contextInitialized completed");
	}*/
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}

}
