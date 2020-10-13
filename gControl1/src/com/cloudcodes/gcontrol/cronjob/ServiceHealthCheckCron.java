package com.cloudcodes.gcontrol.cronjob;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudcodes.gcontrol.pushnotification.PushNotificationCommon;
import com.cloudcodes.gcontrol.utility.ErrorHandler;
import com.cloudcodes.gcontrol.utility.GControlLibrary;

/**
 * Servlet implementation class PushNotificatinServiceCheckCron
 * to check the push notification service in working condition or not
 * @author anand nandeshwar(09-07-2019)
 */
public class ServiceHealthCheckCron extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(this.getClass().getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			new PushNotificationCommon().addBackendTask("PushNotificationService", "/backend/pushnotification/healthcheck", 
					GControlLibrary.PUSH_NOTIFICATION_SERVICE, GControlLibrary.HEALTH_CHECK_QUEUE, GControlLibrary.HTTP_POST);
		}catch(Exception ex) {
			log.warning("***EXCEPTION:OCCUR_WHILE_ADDING_BACKEN_TASK_FOR_HEALTH_CHECK***");
			ErrorHandler.errorHandler(this.getClass().getName(), ex);
		}
	}

}
