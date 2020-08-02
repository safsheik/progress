package com.progrexion.bcm.messaging.models.provisioningchange.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author deprak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String featureCd;
	private String featureId;
	private String featureName;
	private String newFeatureCd;
	private String newFeatureId;
	private String newFeatureName;
	private ProvisioningAction action;

	/**
	 * 
	 */
	public FeatureDTO() {
		super();
	}

	/**
	 * @param featureCd
	 * @param action
	 */
	public FeatureDTO(String featureCd, ProvisioningAction action, String featureId, String featureName) {
		super();
		this.featureCd = featureCd;
		this.action = action;
		this.featureId = featureId;
		this.featureName = featureName;
	}

	/**
	 * @return the featureCd
	 */
	public String getFeatureCd() {
		return featureCd;
	}

	/**
	 * @param featureCd
	 *            the featureCd to set
	 */
	public void setFeatureCd(String featureCd) {
		this.featureCd = featureCd;
	}

	/**
	 * @return the action
	 */
	public ProvisioningAction getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(ProvisioningAction action) {
		this.action = action;
	}

	/**
	 * @return the featureId
	 */
	public String getFeatureId() {
		return featureId;
	}

	/**
	 * @param featureId
	 *            the featureId to set
	 */
	public void setFeatureId(String featureId) {
		this.featureId = featureId;
	}

	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * @param featureName the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	
	@Override
	public String toString(){
		return "FeatureDTO ["
				+ (this.featureId != null ? "featureId=" + this.featureId + ", " : "")
				+ (this.featureName != null ? "featureName=" + this.featureName + ", " : "")
				+ (this.featureCd != null ? "featureCd=" + this.featureCd + ", " : "")
				+ (this.action != null ? "action=" + this.action + ", " : "")
				+"]";
	}

	/**
	 * @return the newFeatureCd
	 */
	public String getNewFeatureCd() {
		return newFeatureCd;
	}

	/**
	 * @param newFeatureCd the newFeatureCd to set
	 */
	public void setNewFeatureCd(String newFeatureCd) {
		this.newFeatureCd = newFeatureCd;
	}

	/**
	 * @return the newFeatureId
	 */
	public String getNewFeatureId() {
		return newFeatureId;
	}

	/**
	 * @param newFeatureId the newFeatureId to set
	 */
	public void setNewFeatureId(String newFeatureId) {
		this.newFeatureId = newFeatureId;
	}

	/**
	 * @return the newFeatureName
	 */
	public String getNewFeatureName() {
		return newFeatureName;
	}

	/**
	 * @param newFeatureName the newFeatureName to set
	 */
	public void setNewFeatureName(String newFeatureName) {
		this.newFeatureName = newFeatureName;
	}
	
	
}
