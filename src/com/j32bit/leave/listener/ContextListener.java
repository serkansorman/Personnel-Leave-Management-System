package com.j32bit.leave.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

import com.j32bit.leave.service.ServiceFacade;



@WebListener
public class ContextListener implements ServletContextListener {
	
	final Logger logger = Logger.getLogger(ContextListener.class);


	@Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
		
		try {
			
			logger.debug("ContextListener started");
			Properties prop = new Properties();
			ServletContext context = servletContextEvent.getServletContext();
			InputStream input = new FileInputStream(context.getRealPath("/WEB-INF/classes/leave.properties"));
			prop.load(input);
			
			ServiceFacade.getInstance().start(prop);
			
			closeInputStream(input);
			
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.debug("ContextListener destroyed");

    }
    
    private void closeInputStream(InputStream input) {
    	
    	if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}