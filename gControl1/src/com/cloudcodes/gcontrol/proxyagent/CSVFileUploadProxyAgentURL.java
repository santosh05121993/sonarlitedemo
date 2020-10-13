package com.cloudcodes.gcontrol.proxyagent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudcodes.gcontrol.businessaccesslayer.ProxyAgentManager;
import com.cloudcodes.gcontrol.common.AuditLogsSetting;
import com.cloudcodes.gcontrol.utility.AuditLogsStatus;
import com.cloudcodes.gcontrol.utility.DesEncrypter;
import com.cloudcodes.gcontrol.utility.ErrorHandler;
import com.cloudcodes.gcontrol.utility.GControlConstants;
import com.cloudcodes.gcontrol.utility.GControlLibrary;
import com.cloudcodes.gcontrol.utility.GControlUtilsCommon;
import com.cloudcodes.gcontrol.utility.UserInfoUtility;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * Servlet implementation class CSVFileUploadProxyAgnetURL
 */
public class CSVFileUploadProxyAgentURL extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final Logger log = Logger.getLogger(this.getClass().getName());
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		JSONObject jsonObject 								 = new JSONObject();
		GControlUtilsCommon f_objGControlUtilsCommon 		 = new GControlUtilsCommon();
		
		try{
			BlobstoreService blobstoreService 	= BlobstoreServiceFactory.getBlobstoreService();
			String userEmail				 	= null;
    		UserInfoUtility userObj 			= new UserInfoUtility();
    		userEmail 							= userObj.getAdminEmail(req, resp);
    		String sDomain 						= null;
    		
    		if (userEmail == null || userEmail.equals("EmailNotfound")) {
    			f_objGControlUtilsCommon.validateJsonResponse(resp, jsonObject, "false", GControlConstants.EMAIL_EMPTY);
				return;
    		}
			try{
				sDomain = userObj.getDomainName(req, resp);
			} catch(Exception e){
				return;
			}
			if(sDomain != null){
				String forWhat  			= req.getParameter("proxyAgentUserForWhat");
				String f_strPolicyId   		= req.getParameter("proxyAgentPolicyIdForUser");
				String tokenFromRequest	 	= req.getParameter("gct");	
				
				ProxyAgentManager f_objProxyAgentManager=	new ProxyAgentManager(sDomain.trim());
				String fileName 						= req.getParameter("proxyAgentCSV");
				Map<String, List<BlobKey>> blobs 		= blobstoreService.getUploads(req);
				List<BlobKey> blobKey 					= blobs.get("proxyAgentCSV"); 
				BlobInfo blobinfo 						= new BlobInfoFactory().loadBlobInfo(blobKey.get(0));
				fileName  								= blobinfo.getFilename();
				SecretKey key         					= DesEncrypter.loadKey(new File("textfiles/secretkey.txt"));
				DesEncrypter encrypter 					= new DesEncrypter(key);
				
				if(f_strPolicyId != null){
					
						processRequest(req, resp, userEmail, sDomain, forWhat,f_strPolicyId, tokenFromRequest,
								f_objProxyAgentManager, fileName, blobKey,encrypter);
				}else {
					
					f_objGControlUtilsCommon.validateJsonResponse(resp, jsonObject, "false", GControlConstants.GROUP_ID_NULL);
					return;
				}
			}else {
				
				f_objGControlUtilsCommon.validateJsonResponse(resp, jsonObject, "false", GControlConstants.DOMAIN_NULL);
				return;
			}
		}catch(Exception e){
			
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param userEmail
	 * @param sDomain
	 * @param forWhat
	 * @param f_strPolicyId
	 * @param tokenFromRequest
	 * @param f_objProxyAgentManager
	 * @param fileName
	 * @param blobKey
	 * @param encrypter
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JSONException
	 */
	private void processRequest(HttpServletRequest req,HttpServletResponse resp, String userEmail, String sDomain,
			String forWhat, String f_strPolicyId, String tokenFromRequest,ProxyAgentManager f_objProxyAgentManager, String fileName,
			List<BlobKey> blobKey, DesEncrypter encrypter){
		
		String encodedGroupId;
		try{

			f_strPolicyId	= URLDecoder.decode(f_strPolicyId,"UTF-8");
			f_strPolicyId 	= encrypter.decrypt(f_strPolicyId);

			f_objProxyAgentManager.addUploadedCSV(sDomain, fileName, blobKey.get(0).getKeyString(),userEmail);
			GControlLibrary gControlLibrary = new GControlLibrary();
			JSONObject jsonParam 			= new JSONObject();
			String type 					= null;

			if(forWhat.equalsIgnoreCase("user")){
				type = "user";
			}else{
				type = "URL";
			}
			try {
				jsonParam.put("sDomain", sDomain);
				jsonParam.put("forWhat", forWhat);
				jsonParam.put("type", type);

				jsonParam.put("policyId", f_strPolicyId);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String url = "/backends/ReadProxyAgentCSV";

			try{
				gControlLibrary.addBackendTask(jsonParam.toString(), url , 
						GControlLibrary.IMPORT_MODULE, 
						GControlLibrary.IMPORT_QUEUE,
						GControlLibrary.HTTP_GET);
			}
			catch(Exception e){
				if(e.getMessage().contains("Identifier Status is unresolved (not a static field)")){
					log.warning("WARNING:In_CSVFileUploadProxyAgentURL_if Identifier_Status_is_unresolved_(not_a_static_field)");

					try{
						gControlLibrary.addBackendTask(jsonParam.toString(), url , 
								GControlLibrary.IMPORT_MODULE, 
								GControlLibrary.IMPORT_QUEUE, 
								GControlLibrary.HTTP_GET);
					}catch(Exception ee){
						for(int i=0;i<e.getStackTrace().length;i++)
						{
							log.warning(" \n "+e.getStackTrace()[i].toString());
						}
					}
				}
				for(int i=0;i<e.getStackTrace().length;i++){
					log.warning(" \n "+e.getStackTrace()[i].toString());
				}
			}
			//it save the audit log
			saveAuditLog(req, resp, userEmail, f_strPolicyId, type);
			encodedGroupId	= URLEncoder.encode(encrypter.encrypt(f_strPolicyId),"UTF-8");
			resp.sendRedirect("/admin/GetPolicyLists?gct="+tokenFromRequest+"&requestForPolicy=proxyAgentPolicyUpdate;"+encodedGroupId+""
					+ "&fromPolicy=true&refreshFor=import");
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @param userEmail
	 * @param f_strPolicyId
	 * @param type
	 */
	private void saveAuditLog(HttpServletRequest req, HttpServletResponse resp,
			String userEmail, String f_strPolicyId, String type) {
		try{
			AuditLogsSetting f_policyData 	= new AuditLogsSetting();
			Object prevObject				= null;
			Object newObject				= null;
			String importFor				= null;
			
			if(type.equalsIgnoreCase("user")){
				importFor = AuditLogsStatus.Import_User_CSV;
		 	}else{
		 		importFor = "Import CSV for URL";
		 	}
			
			f_policyData.addAuditlog(userEmail,GControlConstants.PROXY_AGENT_POLICY,f_strPolicyId,null,
									importFor,req,resp,prevObject,newObject,null);
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
		}
	}

}
