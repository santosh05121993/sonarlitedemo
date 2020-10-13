package com.cloudcodes.gcontrol.cronjob;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloudcodes.gcontrol.businessaccesslayer.GLoginManager;
import com.cloudcodes.gcontrol.dataaccesslayer.Configuration;
import com.cloudcodes.gcontrol.utility.ErrorHandler;
import com.cloudcodes.gcontrol.utility.GControlLibrary;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class StartCustomerPolicyUserCount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	final Logger logger = Logger.getLogger(this.getClass().getName());

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//		if(request.getParameter("sDomain")==null){
//			try{
//
//				GLoginManager gloginObj = new GLoginManager("MASTER");
//
//				List<String> domainList = null;
//
//				try {
//					domainList = gloginObj.getNameSpace();
//				} catch (Exception e) {
//					ErrorHandler.errorHandler(this.getClass().getName(), e);
//					domainList = null;
//				}
//				if(domainList != null){
//
//					logger.warning("Number of Domains :: "+domainList.size()+"\n Domain List :: "+domainList);
//
//					if(domainList != null && !(domainList.isEmpty())){
//
////						for(int j=0;j<domainList.size();j++){
////							try{
////								String domainName = domainList.get(j);
////
////								if(domainName != null ){
////
////									if(domainName.equals("MASTER") || domainName.equals(""))
////										continue;
////
////									String f_StrDefaultVersion =  ModulesServiceFactory.getModulesService().getDefaultVersion("manageclientmodule");
////									Queue queue = QueueFactory.getDefaultQueue();
////									TaskOptions objTskOptions=  TaskOptions.Builder.withUrl("/backends/CustomerPolicyUserCount?DomainName="+domainName+"&activate=forAll")
////											.countdownMillis(2000)
////											.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("manageclientmodule",f_StrDefaultVersion))
////											.method(Method.GET);
////									queue.add(objTskOptions);
////
////									logger.warning("Task added in the queue for Domain Name : "+domainName);
////
////								}
////							}catch(Exception e)
////							{
////								ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
////								logger.warning("Error : "  + e.getMessage());
////							}
////						}
//					}
//				}
//				//				String f_StrDefaultVersion =  ModulesServiceFactory.getModulesService().getDefaultVersion("manageclientmodule");
//				//				Queue queue = QueueFactory.getQueue(GControlLibrary.USER_QUEUE);
//				//				TaskOptions objTskOptions=  TaskOptions.Builder.withUrl("/backends/CustomerPolicyUserCount?activate=forAll")
//				//						.countdownMillis(2000)
//				//						.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("manageclientmodule",f_StrDefaultVersion))
//				//						.method(Method.GET);
//				//				queue.add(objTskOptions);
//				//				logger.info("Task added in the queue.....");
//			}catch(Exception e)
//			{
//				ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
//				logger.warning("Error : "  + e.getMessage());
//			}
//		}
//		else if(request.getParameter("sDomain") !=null)
//		{
////
////			try{
////			String f_StrDefaultVersion =  ModulesServiceFactory.getModulesService().getDefaultVersion("manageclientmodule");
////			Queue queue = QueueFactory.getQueue(GControlLibrary.USER_QUEUE);
////			TaskOptions objTskOptions=  TaskOptions.Builder.withUrl("/backends/CustomerPolicyUserCount?activate=one&DomainName="+request.getParameter("sDomain"))
////					.countdownMillis(2000)
////					.header("Host", ModulesServiceFactory.getModulesService().getVersionHostname("manageclientmodule",f_StrDefaultVersion))
////					.method(Method.GET);
////			queue.add(objTskOptions);
////			logger.info("Task added in the queue.....");
////			}catch(Exception e)
////			{
////				ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
////				logger.warning("Error : "  + e.getMessage());
////			}
//
//
//		}



	}
}

