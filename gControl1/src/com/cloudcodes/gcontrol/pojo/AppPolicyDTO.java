package com.cloudcodes.gcontrol.pojo;

import java.util.Date;

import com.cloudcodes.gcontrol.dataaccesslayer.controls.AppPolicy;

/**
 * This DTO used for transfering data from servlet to JSP
 * @author Harshal Patil
 *
 */
public class AppPolicyDTO {

	String name,policyId,createdBy;
	
	Boolean enable;
	
	Date modifiedOn;
	
	public AppPolicyDTO(){
		
	}
	
	/**
	 * Get some property from policy object and put in object
	 * @param appPolicy
	 */
	public AppPolicyDTO(String policyName,Boolean enable,String policyId,AppPolicy appPolicy){
		this.name = policyName;
		this.enable = enable;
		this.policyId = policyId;
		this.createdBy = appPolicy.getCreatedBy();
		this.modifiedOn = appPolicy.getModifiedOn();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
}
