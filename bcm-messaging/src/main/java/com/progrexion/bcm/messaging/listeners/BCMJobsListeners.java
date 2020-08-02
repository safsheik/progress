package com.progrexion.bcm.messaging.listeners;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.model.v1.ProcessOrderMessageRequestModel;
import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;
import com.progrexion.bcm.services.jobs.processors.JobService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BCMJobsListeners extends BaseListener  {
	@Autowired
	@Qualifier("ordersJobServiceImpl")
	JobService ordersJobServiceImpl;
	
	@JmsListener(destination = "bcmJobsProcessingOrdersQueueDestination", containerFactory = "bcmJobsProcessingOrdersQueueJmsContainerFactory")
	public void onProcessOrderMessage(Message message, Session session) throws Exception {
		log.info("Message received on Job Process Order Queue: {}",
				objectMapper.writeValueAsString(((TextMessage) message).getText()));
		
		ProcessOrderMessageDTO messageDto = convert(message, ProcessOrderMessageDTO.class);
		if (isValidMessage(messageDto)) {
			log.info("Message received from BCM listener is valid to Process for the BCM customer Data Id {}",
					messageDto.getCustomerDataId());
			try{
				ordersJobServiceImpl.processOrderReport(modelMapper.map(messageDto, ProcessOrderMessageRequestModel.class));
				log.info("onProcessOrderMessage(): Completed");
			} catch (BCMModuleException bcmExc) {
				log.error("onProcessOrderMessage()::Exception occurred while processing Customer Orders: {}", bcmExc.getErrorMessage());
			} catch (Exception e) {
				log.error("onProcessOrderMessage()::Error processing Customer Orders: {}", e.getMessage());
			}
		}
	}


	private boolean isValidMessage(ProcessOrderMessageDTO messageDto) {		
		boolean isValid = false;
		
		if (null != messageDto && null != messageDto.getCustomerDataId()
				&& null != messageDto.getJobId()
				&& null != messageDto.getCustomerOrderProcessId()) {
			isValid = true;
		}
		return isValid;
	}
}
