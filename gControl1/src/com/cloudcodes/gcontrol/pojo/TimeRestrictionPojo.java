/**
 * 
 */
package com.cloudcodes.gcontrol.pojo;

import java.util.Date;

import com.google.appengine.api.datastore.Text;

/**
 * @author Swapnil Lipare
 *
 */
public class TimeRestrictionPojo{

	
	private Long groupId;
	
	private Date createdOn;
	
	private String groupName;
	
	private String groupName_Lower;

	private Text groupDescription;
	
	private boolean timeRestEnabled;
	
	private Boolean sunEnabled;
	
	private Boolean monEnabled;
	
	private Boolean tueEnabled;
	
	private Boolean wedEnabled;
	
	private Boolean thuEnabled;
	
	private Boolean friEnabled;
	
	private Boolean satEnabled;
	
	private String timeZone;
	
	private String timeZoneDisplay;
	
	private String timeIn;
	
	private String timeOut;
	
	private Boolean validAlways;
	
	private Date validFrom;
	
	private Date validUpTo;
	
	private Boolean nextDay;
	
	private Boolean forceLogout;
	
	private String basedOn;
	
	private String riskLevel;

	/**
	 * @return
	 */
	public Long getId() {
		return groupId;
	}

	/**
	 * @param groupId
	 */
	public void setId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the groupName_Lower
	 */
	public String getGroupName_Lower() {
		return groupName_Lower;
	}

	/**
	 * @return the groupDescription
	 */
	public Text getGroupDescription() {
		return groupDescription;
	}

	/**
	 * @return the timeRestEnabled
	 */
	public boolean isTimeRestEnabled() {
		return timeRestEnabled;
	}

	/**
	 * @return the sunEnabled
	 */
	public Boolean getSunEnabled() {
		return sunEnabled;
	}

	/**
	 * @return the monEnabled
	 */
	public Boolean getMonEnabled() {
		return monEnabled;
	}

	/**
	 * @return the tueEnabled
	 */
	public Boolean getTueEnabled() {
		return tueEnabled;
	}

	/**
	 * @return the wedEnabled
	 */
	public Boolean getWedEnabled() {
		return wedEnabled;
	}

	/**
	 * @return the thuEnabled
	 */
	public Boolean getThuEnabled() {
		return thuEnabled;
	}

	/**
	 * @return the friEnabled
	 */
	public Boolean getFriEnabled() {
		return friEnabled;
	}

	/**
	 * @return the satEnabled
	 */
	public Boolean getSatEnabled() {
		return satEnabled;
	}

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @return the timeZoneDisplay
	 */
	public String getTimeZoneDisplay() {
		return timeZoneDisplay;
	}

	/**
	 * @return the timeIn
	 */
	public String getTimeIn() {
		return timeIn;
	}

	/**
	 * @return the timeOut
	 */
	public String getTimeOut() {
		return timeOut;
	}

	/**
	 * @return the validAlways
	 */
	public Boolean getValidAlways() {
		return validAlways;
	}

	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}

	/**
	 * @return the validUpTo
	 */
	public Date getValidUpTo() {
		return validUpTo;
	}

	/**
	 * @return the nextDay
	 */
	public Boolean getNextDay() {
		return nextDay;
	}

	/**
	 * @return the forceLogout
	 */
	public Boolean getForceLogout() {
		return forceLogout;
	}

	/**
	 * @return the basedOn
	 */
	public String getBasedOn() {
		return basedOn;
	}

	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @param groupName_Lower the groupName_Lower to set
	 */
	public void setGroupName_Lower(String groupName_Lower) {
		this.groupName_Lower = groupName_Lower;
	}

	/**
	 * @param groupDescription the groupDescription to set
	 */
	public void setGroupDescription(Text groupDescription) {
		this.groupDescription = groupDescription;
	}

	/**
	 * @param timeRestEnabled the timeRestEnabled to set
	 */
	public void setTimeRestEnabled(boolean timeRestEnabled) {
		this.timeRestEnabled = timeRestEnabled;
	}

	/**
	 * @param sunEnabled the sunEnabled to set
	 */
	public void setSunEnabled(Boolean sunEnabled) {
		this.sunEnabled = sunEnabled;
	}

	/**
	 * @param monEnabled the monEnabled to set
	 */
	public void setMonEnabled(Boolean monEnabled) {
		this.monEnabled = monEnabled;
	}

	/**
	 * @param tueEnabled the tueEnabled to set
	 */
	public void setTueEnabled(Boolean tueEnabled) {
		this.tueEnabled = tueEnabled;
	}

	/**
	 * @param wedEnabled the wedEnabled to set
	 */
	public void setWedEnabled(Boolean wedEnabled) {
		this.wedEnabled = wedEnabled;
	}

	/**
	 * @param thuEnabled the thuEnabled to set
	 */
	public void setThuEnabled(Boolean thuEnabled) {
		this.thuEnabled = thuEnabled;
	}

	/**
	 * @param friEnabled the friEnabled to set
	 */
	public void setFriEnabled(Boolean friEnabled) {
		this.friEnabled = friEnabled;
	}

	/**
	 * @param satEnabled the satEnabled to set
	 */
	public void setSatEnabled(Boolean satEnabled) {
		this.satEnabled = satEnabled;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @param timeZoneDisplay the timeZoneDisplay to set
	 */
	public void setTimeZoneDisplay(String timeZoneDisplay) {
		this.timeZoneDisplay = timeZoneDisplay;
	}

	/**
	 * @param timeIn the timeIn to set
	 */
	public void setTimeIn(String timeIn) {
		this.timeIn = timeIn;
	}

	/**
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * @param validAlways the validAlways to set
	 */
	public void setValidAlways(Boolean validAlways) {
		this.validAlways = validAlways;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @param validUpTo the validUpTo to set
	 */
	public void setValidUpTo(Date validUpTo) {
		this.validUpTo = validUpTo;
	}

	/**
	 * @param nextDay the nextDay to set
	 */
	public void setNextDay(Boolean nextDay) {
		this.nextDay = nextDay;
	}

	/**
	 * @param forceLogout the forceLogout to set
	 */
	public void setForceLogout(Boolean forceLogout) {
		this.forceLogout = forceLogout;
	}

	/**
	 * @param basedOn the basedOn to set
	 */
	public void setBasedOn(String basedOn) {
		this.basedOn = basedOn;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	
}
