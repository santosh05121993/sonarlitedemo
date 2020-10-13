package com.cloudcodes.gcontrol.cronjob;

import javax.servlet.http.HttpServlet;

//import com.cloudcodes.BusinessAccessLayer.GLoginManager;
//import com.cloudcodes.DataAccessLayer.Configuration;
//import com.cloudcodes.DataAccessLayer.ForgotPass;
//import com.cloudcodes.DataAccessLayer.FrgtPassSettings;
//import com.cloudcodes.DataAccessLayer.MigrationReportOfForgotPass;
//import com.cloudcodes.DataAccessLayer.QuestionBank;
//import com.cloudcodes.glogin.ErrorHandler;
//import com.google.api.services.admin.directory.model.OrgUnit;
//import com.google.appengine.api.datastore.Text;

public class MigrateForgotPwd extends HttpServlet{
	/*private static final long serialVersionUID = 1L;

	final Logger log = Logger.getLogger(this.getClass().getName());
	String  SERVICE_ACCOUNT_EMAIL = null;
	*//** Path to the Service Account's Private Key file *//*
	String SERVICE_ACCOUNT_PKCS12_FILE_PATH = null;
	
	public void init(ServletConfig config) throws ServletException {
		log.warning("In init method");
		super.init(config);
		try {
			SERVICE_ACCOUNT_EMAIL = getServletContext().getInitParameter("SERVICE_ACCOUNT_EMAIL");
			SERVICE_ACCOUNT_PKCS12_FILE_PATH = getServletContext().getInitParameter("SERVICE_ACCOUNT_PKCS12_FILE_PATH");
			log.warning("Out init method");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try{
			log.warning("start");
			GLoginManager gloginObj = new GLoginManager("MASTER");
			
			List<MigrationReportOfForgotPass> lstMigrationReportOfFP = gloginObj.retriveAllDomainsMigrtionInfoForFP();
			if(lstMigrationReportOfFP == null || lstMigrationReportOfFP.isEmpty()){
				log.warning("Migration report of forgot pasword is empty");
				java.util.List<String> namespacesList = gloginObj.getNameSpace();
				log.warning("Domain Name List : " + namespacesList);
				
				if(namespacesList != null){
					for(int j=0;j<namespacesList.size();j++){
						try{
							String domain = namespacesList.get(j);
							MigrationReportOfForgotPass migrateFPObj = new MigrationReportOfForgotPass();
							migrateFPObj.setDomainName(domain);
							migrateFPObj.setStatus("New");
							gloginObj.addForgotPassMigrationObj(migrateFPObj);
						}
						catch(Exception e)
						{
							ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
							log.warning("Error : "  + e.getMessage());
						}
					}
					log.info("Records for all domains added in the table.....");
					for(int j=0;j<namespacesList.size();j++){
						try{
							String domain = namespacesList.get(j);
							GLoginManager domainManagerObj = new GLoginManager(domain);
							FrgtPassSettings fpObj = domainManagerObj.getFrgtPassSettings();
							Configuration conObj = domainManagerObj.getConfiguration();
							if(fpObj == null)
								log.warning("The policy does not exist for the domain : " + domain);
							else if(fpObj.isForgotPassEnabled() == false)
								log.warning("The policy is not enabled for the domain : " + domain);
							else{
								log.warning("Migrating policy for the domain : " + domain);
								ForgotPass FPObj = createNewForgotPassPolicy(fpObj);
								
								//add domain level OU to the ForgotPassOUs table 
//								gloginObj.addForgotPassOU(policyId,domain);
								List<OrgUnit> orgList = domainManagerObj.getOrgUnits(SERVICE_ACCOUNT_EMAIL,SERVICE_ACCOUNT_PKCS12_FILE_PATH,conObj.getAdminEmail());
								if(orgList != null && orgList.isEmpty() == false){
									
									String policyId = domainManagerObj.saveForgotPassSettings(FPObj);
									log.warning("New forgot password policy created successfully");
									log.warning("Policy ID : " + policyId);
									
									domainManagerObj.addForgotPassOU(policyId,domain);
									
									for(int k=0 ; k < orgList.size() ; k++){
										try{
											String ouName = orgList.get(k).getName();
								 			String orgPath = orgList.get(k).getOrgUnitPath();
								 			String parentPath = orgList.get(k).getParentOrgUnitPath();
								 			log.warning("ouName = " + ouName);
								 			log.warning("orgpath = " + orgPath);
								 			log.warning("parentPath = " + parentPath);
								 			if(orgPath.equals("/")){
								 				orgPath = domain;
								 			}
								 			domainManagerObj.addForgotPassOU(policyId,orgPath);
										} catch (Exception e) {
										}
									}
									log.warning("moving question bank...");
									//Move old question bank to new policy
									List<QuestionBank> lstQuestoinBank = domainManagerObj.getAllQuestions(null);
									if(lstQuestoinBank == null || lstQuestoinBank .isEmpty()){
										log.warning("lstQuestoinBank null");
									}
									else 
									{
										log.warning("lstQuestoinBank not null");
										for(int z =0 ; z< lstQuestoinBank.size() ; z++){
											try{
												QuestionBank tmpQueBank = lstQuestoinBank.get(z);
												if(tmpQueBank.getPolicyId() == null){
													log.warning("PolicyId is null");
													tmpQueBank.setPolicyId(policyId);
													domainManagerObj.addQuestion(tmpQueBank);
												}
												else
													log.warning("PolicyId is " + tmpQueBank.getPolicyId());
												
											}catch (Exception e) {
												log.warning("error : " + e.getMessage());
											}
										}
										log.warning("question bank moved successfully...");
									}
									
								} else {
									MigrationReportOfForgotPass migratePPObj = gloginObj.retrieveFPMigrationInfo(domain);
									if(migratePPObj != null){
										gloginObj.addCommentToMigrateFP(migratePPObj,"Organisation List is null");
									}
									continue;
								}
								
								MigrationReportOfForgotPass migratePPObj = gloginObj.retrieveFPMigrationInfo(domain);
								if(migratePPObj != null){
									gloginObj.updateMigrateFPStatus(migratePPObj);
									log.warning("Data migrated successfully");
								}
								else 
									log.warning("Data not found");
							}
						}
						catch(Exception e)
						{
							ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
							log.warning("Error : "  + e.getMessage());
						}
					}
				}
			} else{
				log.warning("Migration reports of forgot pasword policy is not empty.........");
				
				for(int j=0;j<lstMigrationReportOfFP.size();j++){
					try{
						String domain = lstMigrationReportOfFP.get(j).getDomainName();
						GLoginManager domainManagerObj = new GLoginManager(domain);
						FrgtPassSettings passPolicyObj = domainManagerObj.getFrgtPassSettings();
						Configuration conObj = domainManagerObj.getConfiguration();
						if(passPolicyObj == null)
							log.warning("The policy does not exist for the domain : " + domain);
						else if(passPolicyObj.isForgotPassEnabled() == false)
							log.warning("The policy is not enabled for the domain : " + domain);
						else{
							log.warning("Migrating policy for the domain : " + domain);
							ForgotPass PPObj = createNewForgotPassPolicy(passPolicyObj);
							
							//add domain level OU to the ForgotPassOUs table 
//							gloginObj.addForgotPassOU(policyId,domain);
							List<OrgUnit> orgList = domainManagerObj.getOrgUnits(SERVICE_ACCOUNT_EMAIL,SERVICE_ACCOUNT_PKCS12_FILE_PATH,conObj.getAdminEmail());
							if(orgList != null && orgList.isEmpty() == false){
								String policyId = domainManagerObj.saveForgotPassSettings(PPObj);
								log.warning("New forgot password policy created successfully");
								log.warning("Policy ID : " + policyId);
								
								domainManagerObj.addForgotPassOU(policyId,domain);
								
								for(int k=0 ; k < orgList.size() ; k++){
									try{
										String ouName = orgList.get(k).getName();
							 			String orgPath = orgList.get(k).getOrgUnitPath();
							 			String parentPath = orgList.get(k).getParentOrgUnitPath();
							 			log.warning("ouName = " + ouName);
							 			log.warning("orgpath = " + orgPath);
							 			log.warning("parentPath = " + parentPath);
							 			if(orgPath.equals("/")){
							 				orgPath = domain;
							 			}
							 			domainManagerObj.addForgotPassOU(policyId,orgPath);
									} catch (Exception e) {
									}
								}
								log.warning("moving question bank...");
								//Move old question bank to new policy
								List<QuestionBank> lstQuestoinBank = domainManagerObj.getAllQuestions(null);
								if(lstQuestoinBank == null || lstQuestoinBank .isEmpty()){
									log.warning("lstQuestoinBank null");
								}
								else 
								{
									log.warning("lstQuestoinBank not null");
									for(int z =0 ; z< lstQuestoinBank.size() ; z++){
										try{
											QuestionBank tmpQueBank = lstQuestoinBank.get(z);
											if(tmpQueBank.getPolicyId() == null){
												log.warning("PolicyId is null");
												tmpQueBank.setPolicyId(policyId);
												domainManagerObj.addQuestion(tmpQueBank);
											}
											else
												log.warning("PolicyId is " + tmpQueBank.getPolicyId());
										}catch (Exception e) {
											log.warning("error : " + e.getMessage());
										}
									}
									log.warning("question bank moved successfully...");
								}
							} else {
								MigrationReportOfForgotPass migratePPObj = lstMigrationReportOfFP.get(j);
								
								gloginObj.addCommentToMigrateFP(migratePPObj,"Organisation List is null");
								continue;
							}
							
							MigrationReportOfForgotPass migratePPObj = lstMigrationReportOfFP.get(j);
							
							gloginObj.updateMigrateFPStatus(migratePPObj);
							log.warning("Data migrated successfully");
						}
					}
					catch(Exception e)
					{
						ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
						log.warning("Error : "  + e.getMessage());
					}
				}
			}
		}
		catch(Exception e)
		{
			ErrorHandler.errorHandler(this.getClass().getSimpleName(),e);
			log.warning("Error : "  + e.getMessage());
		}
	}
	*//**
	 * @param passPolicyObj
	 * @param conObj
	 * @return
	 *//*
	private ForgotPass createNewForgotPassPolicy(FrgtPassSettings passPolicyObj) {
		ForgotPass PPObj = new ForgotPass();
		try {
			PPObj.setPolicyName("My Forgot Password Policy");
			PPObj.setPolicyDescription(new Text("Forgot password policy for all domain users."));
			PPObj.setPolicyEnabled(true);
			PPObj.setValidAlways(true);
			PPObj.setNumOfQustns(passPolicyObj.getNoOfQuestions());
			PPObj.setNumOfAttmpts(passPolicyObj.getNoOfAttempts());
			PPObj.setNumOfSkipAttmpts(10); //This field was not exist in the previous settings. So, I have set Max Value.
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PPObj;
	}*/
}