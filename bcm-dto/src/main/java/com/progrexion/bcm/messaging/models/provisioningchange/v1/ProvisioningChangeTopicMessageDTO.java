/**
 * 
 */
package com.progrexion.bcm.messaging.models.provisioningchange.v1;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.progrexion.bcm.messaging.models.common.AbstractMessageDto;
import com.progrexion.bcm.models.v1.ProvisioningChangeDto;

/**
 * @author deprak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvisioningChangeTopicMessageDTO extends AbstractMessageDto{

	private static final long serialVersionUID = 1L;
	private List<FeatureDTO> featureDTOList;
	private Long subscriptionId;
	private Long newSubscriptionId;
	private Long externalId;
	private List<Long> mergedUcids;
	private String channel;
	
	
	/**
	 * @param ucid
	 * @param brand
	 * @param featureDTOList
	 * @param subscriptionId
	 * @param externalId
	 * @param ucidList
	 */
	public ProvisioningChangeTopicMessageDTO(Long ucid, String brand, List<FeatureDTO> featureDTOList,
			Long subscriptionId, Long externalId, List<Long> mergedUcids,String channel) {
		super();
		this.ucid = ucid;
		this.brand = brand;
		this.featureDTOList = featureDTOList;
		this.subscriptionId = subscriptionId;
		this.channel = channel;
		this.setExternalId(externalId);
		this.setMergedUcids(mergedUcids);
	}
	
	/**
	 * @param ucid
	 * @param brand
	 * @param featureDTOList
	 * @param subscriptionId
	 * @param externalId
	 * @param ucidList
	 */	
	public ProvisioningChangeTopicMessageDTO(ProvisioningChangeDto provisioningChangeDto, List<FeatureDTO> featureDTOList, List<Long> mergedUcids) {
		super();
		this.ucid = provisioningChangeDto.getUcid();
		this.brand =provisioningChangeDto.getBrand();
		this.featureDTOList = featureDTOList;
		this.subscriptionId = provisioningChangeDto.getSubscriptionId();
		this.channel = provisioningChangeDto.getChannel();
		this.newSubscriptionId=provisioningChangeDto.getNewSubscriptionId();
		this.setExternalId(provisioningChangeDto.getExternalId());
		this.setMergedUcids(mergedUcids);
	}
	
	/**
	 * 
	 */
	public ProvisioningChangeTopicMessageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the featureDTOList
	 */
	public List<FeatureDTO> getFeatureDTOList() {
		return featureDTOList;
	}
	/**
	 * @param featureDTOList the featureDTOList to set
	 */
	public void setFeatureDTOList(List<FeatureDTO> featureDTOList) {
		this.featureDTOList = featureDTOList;
	}
	/**
	 * @return the subscriptionId
	 */
	public Long getSubscriptionId() {
		return subscriptionId;
	}
	/**
	 * @param subscriptionId the subscriptionId to set
	 */
	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	/**
	 * @return the externalId
	 */
	public Long getExternalId() {
		return externalId;
	}

	/**
	 * @param externalId the externalId to set
	 */
	public void setExternalId(Long externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return the mergedUcids
	 */
	public List<Long> getMergedUcids() {
		return mergedUcids;
	}

	/**
	 * @param mergedUcids the mergedUcids to set
	 */
	public void setMergedUcids(List<Long> mergedUcids) {
		this.mergedUcids = mergedUcids;
	}
	
	/**
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	
	
	/**
	 * @return the newSubscriptionId
	 */
	public Long getNewSubscriptionId() {
		return newSubscriptionId;
	}

	/**
	 * @param newSubscriptionId the newSubscriptionId to set
	 */
	public void setNewSubscriptionId(Long newSubscriptionId) {
		this.newSubscriptionId = newSubscriptionId;
	}

	@Override
	public String toString() {
		return "ProvisioningChangeMessageDto [" 
				+ (this.subscriptionId != null ? "subscriptionId=" + this.subscriptionId + ", " : "")
				+ (this.newSubscriptionId != null ? "newSubscriptionId=" + this.newSubscriptionId + ", " : "")
				+ (this.featureDTOList != null ? "featureDTOList=" + this.featureDTOList + ", " : "")
				+ (this.mergedUcids != null ? "mergedUcids=" + this.mergedUcids + ", " : "")
				+ (this.channel != null ? "channel=" + this.channel + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString() : "") + "]";
	}
	
	
}
