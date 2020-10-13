package com.cloudcodes.gcontrol.proxyagent;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cloudcodes.gcontrol.businessaccesslayer.ProxyAgentManager;
import com.cloudcodes.gcontrol.dataaccesslayer.proxyagent.WebCategoryMaster;
import com.cloudcodes.gcontrol.utility.DesEncrypter;
import com.cloudcodes.gcontrol.utility.ErrorHandler;
import com.cloudcodes.gcontrol.utility.GControlConstants;
import com.cloudcodes.gcontrol.utility.JsonKey;

/**
 * Servlet implementation class CheckSimilarWebCategoryURLController
 */
public class CheckSimilarWebCategoryURLController extends HttpServlet {
	
	
	/* 
	 * Response Codes
	 * 
	 * 1) 200 : success "site url found"
	 * 2) 401 : site url not found in database and in similar web api 
	 * 3) 204 : unable to decrypt json or json is null
	 * 
	 * 
	*/
	
	private static final long serialVersionUID 	= 1L;
	final Logger log 							= Logger.getLogger(this.getClass().getName());

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.warning("deleteWebURLMaster-"+request.getParameter("deleteWebURLMaster"));
		if(request.getParameter("deleteWebURLMaster") != null) {
			
			ProxyAgentManager f_objProxyAgentManager = new ProxyAgentManager(GControlConstants.MASTER);
			f_objProxyAgentManager.deleteWebURLMaster();
			
			f_objProxyAgentManager.deleteWebCategoryMaster();
		}else {
			processRequest(request, response);
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response) {
		
		String  jsonReq  				= request.getHeader("message");
		String f_strSiteUrl   			= null;
		JSONObject jsonObject 			= null;
		try{
			JSONObject outerJson = new JSONObject();

			if((jsonReq == null || jsonReq.isEmpty()) ){
				outerJson.put("status", 204);
				response.getWriter().println(encryptJson(outerJson.toString()));
				return;
			}
			
			ProxyAgentManager f_objProxyAgentManager = new ProxyAgentManager(GControlConstants.MASTER);
			try {
				jsonObject = new JSONObject(decryptJson(jsonReq));
			}catch(Exception e){
				ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
				outerJson.put("status", 204);
				response.getWriter().println(encryptJson(outerJson.toString()));
				return;
			}
			if(jsonObject != null && jsonObject.has("siteurl")){
				
				log.warning("site-url-"+jsonObject.get("siteurl"));
				f_strSiteUrl   =  (String)jsonObject.get("siteurl");
				if(f_strSiteUrl != null) {
					//it check If URL present in datastore then it get Category
//					WebCategoryMaster f_objWebCategoryMaster = f_objProxyAgentManager.getWebURLCategoryBySiteUrl(f_strSiteUrl);
					List<WebCategoryMaster> objWebCategoryMasterList = f_objProxyAgentManager.getWebURLCategoryBySiteUrlList(f_strSiteUrl);
					if(objWebCategoryMasterList != null) {
						//it get Category URL Json
						getCatgeoryURLJson(response, outerJson, objWebCategoryMasterList);
						return;
					}else {
//						it call Similar Web API to get Category of url
						getSimilarWebURLCategoryAPI(response, f_strSiteUrl, outerJson);
						return;
					}
				}else {
					log.warning("site url not found");
					outerJson.put("status", 401);
					response.getWriter().println(encryptJson(outerJson.toString()));
					return;
				}
			}
	}catch(Exception e){
		ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
		return;
	}
	}

	/**
	 * it call Similar Web API to get Category of url
	 * 
	 * @param response
	 * @param f_strSiteUrl
	 * @param outerJson
	 */
	private void getSimilarWebURLCategoryAPI(HttpServletResponse response, String strSiteUrl, JSONObject outerJson) {
		
		List<WebCategoryMaster> webCategoryMasterList = null;
		//it get category from similar web api if not found in datastore
		GetSimilarWebURLCategoryAPI objGetSimilarWebURLCategory 	= new GetSimilarWebURLCategoryAPI();
		
		//it get url from similar web by calling similar web api
//		String objResult = f_objGetSimilarWebURLCategory.getSimilarWebCategoryBySiteURL(f_strSiteUrl);
		
		//it get url categories by calling webshrinker api
		String objResult = objGetSimilarWebURLCategory.getWebShrinkerCategoryBySiteURL(strSiteUrl);
		
		if(objResult != null) {
//			webCategoryMasterList = objGetSimilarWebURLCategory.saveSimilarWebCategory(objResult);
			webCategoryMasterList = objGetSimilarWebURLCategory.saveWebshrinkerCategory(objResult);
			if(webCategoryMasterList != null) {
				getCatgeoryURLJson(response, outerJson, webCategoryMasterList);
				return;
			}else {
				log.warning("site url not found");
				getDataNotFoundJson(response, strSiteUrl, outerJson);
				return;
			}
		}else {
			log.warning("site url not found");
			getDataNotFoundJson(response, strSiteUrl, outerJson);
			return;
		}
	}

	/**
	 * it get Category URL Json
	 * 
	 * @param response
	 * @param outerJson
	 * @param f_objWebCategoryMaster
	 * @throws JSONException
	 * @throws IOException
	 */
	private void getCatgeoryURLJson(HttpServletResponse response, JSONObject outerJson,
			List<WebCategoryMaster> objWebCategoryMasterList) {
		try {
			JSONObject responsejson = null;
			JSONArray jarray = new JSONArray();
			for(WebCategoryMaster objWebCategoryMaster : objWebCategoryMasterList) {
				responsejson = new JSONObject();
				responsejson.put("status", 200);
				responsejson.put("siteurl", objWebCategoryMaster.getSiteUrl() != null ? objWebCategoryMaster.getSiteUrl() : "null");
				responsejson.put("categoryid", objWebCategoryMaster.getCategoryId() != null ? objWebCategoryMaster.getCategoryId() : "null");
//				responsejson.put("category", objWebCategoryMaster.getCategory() != null ?  String.join(" ", objWebCategoryMaster.getCategory().split("_")) : "null");
//				responsejson.put("subcategory", objWebCategoryMaster.getSubCategory() != null ? String.join(" ", objWebCategoryMaster.getSubCategory().split("_")): "null");
				responsejson.put("category", objWebCategoryMaster.getCategory() != null ?  objWebCategoryMaster.getCategory() : "null");
				responsejson.put("subcategory", objWebCategoryMaster.getSubCategory() != null ? objWebCategoryMaster.getSubCategory(): "not found");
				responsejson.put("path", objWebCategoryMaster.getPath() != null ? objWebCategoryMaster.getPath() : "null");
				responsejson.put("createddate",  objWebCategoryMaster.getCreatedDate() != null ? objWebCategoryMaster.getCreatedDate() : new Date());
				responsejson.put("modifieddate", objWebCategoryMaster.getModifiedDate() != null ? objWebCategoryMaster.getModifiedDate() : new Date());
				responsejson.put("parentcategoryid", objWebCategoryMaster.getParentCategoryId() != null ? objWebCategoryMaster.getParentCategoryId() : "null" );
				jarray.put(responsejson);
			}
			outerJson.put(JsonKey.W_CATEGORIES, jarray);
			log.warning("outer-json-"+outerJson.toString());
			response.getWriter().println(encryptJson(outerJson.toString()));
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
			return;
		}
	}

	/**
	 * @param response
	 * @param f_strSiteUrl
	 * @param outerJson
	 * @throws JSONException
	 * @throws IOException
	 */
	private void getDataNotFoundJson(HttpServletResponse response, String f_strSiteUrl, JSONObject outerJson){
		
		try {
			outerJson.put("status", 401);
			outerJson.put("errormsg", "Data not found");
			outerJson.put("siteurl", f_strSiteUrl);
			outerJson.put("categoryid", "null");
			outerJson.put("category", "null");
			outerJson.put("subcategory", "null");
			outerJson.put("path", "null");
			outerJson.put("createddate", new Date());
			outerJson.put("modifieddate", new Date());
			outerJson.put("parentcategoryid", "null");
			response.getWriter().println(encryptJson(outerJson.toString()));
			return;
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
			return;
		}
	}
		
	/**
	 * it encrypt Json 
	 * 
	 * @param p_strparam
	 * @return
	 * @throws IOException
	 */
	public String encryptJson(String p_strparam){
		
		try {
			ServletContext f_objServletContext 			= getServletContext();
			String keyPath 		   = f_objServletContext.getRealPath("textfiles/keyfile.txt");
			SecretKey key          = DesEncrypter.loadKey(new java.io.File(keyPath));
			DesEncrypter encrypter = new DesEncrypter(key);
			return encrypter.encrypt(p_strparam);
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
		}
		return p_strparam;
	}
	/**
	 * it decrypt Json
	 * 
	 * @param p_strparam
	 * @return
	 * @throws IOException
	 */
	public String decryptJson(String p_strparam) {
		
		try {
			ServletContext f_objServletContext 			= getServletContext();
			String keyPath 			= f_objServletContext.getRealPath("textfiles/keyfile.txt");
			SecretKey key          	= DesEncrypter.loadKey(new java.io.File(keyPath));
			DesEncrypter encrypter  = new DesEncrypter(key);
			return encrypter.decrypt(p_strparam);
		}catch(Exception e){
			ErrorHandler.errorHandler(this.getClass().getSimpleName(), e);
		}
		return p_strparam;
	}

}
