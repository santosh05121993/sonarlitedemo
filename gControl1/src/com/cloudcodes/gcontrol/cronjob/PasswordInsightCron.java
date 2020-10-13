package com.cloudcodes.gcontrol.cronjob;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudcodes.gcontrol.businessaccesslayer.GLoginManager;
import com.cloudcodes.gcontrol.dataaccesslayer.SSOCustomerDetails;
import com.cloudcodes.gcontrol.utility.ErrorHandler;
import com.cloudcodes.gcontrol.utility.GControlLibrary;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * Servlet implementation class PasswordInsightCron
 */
public class PasswordInsightCron extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final  Logger logger	=	Logger.getLogger(this.getClass().getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		GLoginManager gloginObj = new GLoginManager("MASTER");
		
		List<SSOCustomerDetails> SSOCustomerDetails = gloginObj.findallDomain();
	
		logger.warning("SSOCustomerDetails in passwordinsight cron"+SSOCustomerDetails);
		
		// Check null SSOCustomerDetails
		
		for(int j=0;j<SSOCustomerDetails.size();j++){
			try{
				String domain = SSOCustomerDetails.get(j).getDomainName();
				logger.warning("In passwordinsight cron domain name"+domain);
				if(domain==null || domain.equals("MASTER") || domain.equals(""))
					continue;
		Queue queue = QueueFactory.getQueue(GControlLibrary.USER_QUEUE); 
		String f_StrDefaultVersion =  ModulesServiceFactory.getModulesService().getDefaultVersion("googleuserssync");
		TaskOptions objTskOptions=  TaskOptions.Builder.withUrl("/backends/PasswordInsights?domainName="+domain)
				.countdownMillis(2000)														
				.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("googleuserssync",f_StrDefaultVersion))
				.method(Method.GET)
				.retryOptions(RetryOptions.Builder.withTaskRetryLimit(1).maxDoublings(3));
		
		queue.add(objTskOptions);
		logger.warning("In passwordinsight cron objTskOptions"+objTskOptions);

		response.getWriter().println("true");
	}catch(Exception ee)
			{
		ErrorHandler.errorHandler(this.getClass().getSimpleName(), ee);
	}
		}
	}

}
