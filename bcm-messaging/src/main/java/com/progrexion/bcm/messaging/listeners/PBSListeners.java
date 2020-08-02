package com.progrexion.bcm.messaging.listeners;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.model.v1.CancelSubscriptionMessageRequestModel;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.FeatureDTO;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.ProvisioningAction;
import com.progrexion.bcm.messaging.models.provisioningchange.v1.ProvisioningChangeTopicMessageDTO;
import com.progrexion.bcm.services.v1.MessagingService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PBSListeners extends BaseListener {
	@Value("#{'${messageprocessor.cancelsubscription.criteria.brands:CCOM,}'.split(',')}")
	private List<String> cancelSubscriptionValidBrands;

	@Value("#{'${messageprocessor.cancelsubscription.criteria.featureCode:Rent_Track,}'.split(',')}")
	private List<String> cancelSubscriptionValidFeatureCodes;
	
	@Autowired
	private MessagingService messagingService;

	@JmsListener(destination = "pbsProvisioningChangeTopicDestination", containerFactory = "pbsProvisioningChangeTopicJmsListenerContainerFactory")
	public void onProvisioningChangeMessage(Message message, Session session) throws Exception {
		log.info("Message received on PBS Provisioning Change Topic: {}",
				objectMapper.writeValueAsString(((TextMessage) message).getText()));
		
		ProvisioningChangeTopicMessageDTO messageDto = convert(message, ProvisioningChangeTopicMessageDTO.class);
		
		if (isEligibleForCancelSubscription(messageDto)) {
			log.info("Message received from PBS is eligible for RentTrack Cancel Subscription operation. BRAND: {}, UCID: {}, MERGED-UCIDS: {}",
					messageDto.getBrand(), messageDto.getUcid(), messageDto.getMergedUcids());
			try{
				messagingService.processCancelSubscriptionMessage(modelMapper.map(messageDto, CancelSubscriptionMessageRequestModel.class), message.toString());
			} catch (BCMModuleException bcmExc) {
				log.error("Exception occurred while processing Cancel Subscription: {}", bcmExc.getErrorMessage());
			} catch (Exception e) {
				log.error("Error processing Cancel Subscription: {}", e.getMessage());
			}
		}
	}

	public void onProvisioningChangeMessageV2(ProvisioningChangeTopicMessageDTO message) throws JsonProcessingException, JMSException {
		log.info("Message received on PBS Provisioning Change Topic: {}",
				objectMapper.writeValueAsString(((TextMessage) message).getText()));
	}

	private Boolean isEligibleForCancelSubscription(ProvisioningChangeTopicMessageDTO messageDto) {
		/**
		 *  IF messageDto.brand is a supported Brand, 
		 *  AND action = 'REMOVE', 
		 *  AND messageDto.featureDTOList has a feature with featureCd = '$cancelSubscriptionValidFeatureCode', 
		 *  THEN return true to process the message
		 */			
		log.debug("cancelSubscriptionValidBrands = [{}]", cancelSubscriptionValidBrands);
		log.debug("cancelSubscriptionValidFeatureCode = [{}]", cancelSubscriptionValidFeatureCodes);
		
		if (messageDto != null && !StringUtils.isEmpty(messageDto.getBrand())
				&& cancelSubscriptionValidBrands.contains(messageDto.getBrand())
				&& messageDto.getFeatureDTOList() != null
				&& !messageDto.getFeatureDTOList().isEmpty()) {
			for (FeatureDTO feature : messageDto.getFeatureDTOList()) {
				if (feature.getAction() != null && feature.getAction().equals(ProvisioningAction.REMOVE)
						&& !StringUtils.isEmpty(feature.getFeatureCd())
						&& cancelSubscriptionValidFeatureCodes.contains(feature.getFeatureCd())) {
					return true;
				}
			}				
			return false;
			
		} else {
			return false;
		}

	}

}
