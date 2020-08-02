package com.progrexion.bcm.services.jobs.publisher;

import static com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO.VERSION;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import com.progrexion.bcm.jobs.v1.ProcessOrderMessageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerProcessOrderMessagePublisher {

	@Autowired
	@Qualifier("bcmJobsProcessingOrdersQueueJmsTemplate")
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("bcmJobsProcessingOrdersQueueDestination")
	private Destination destination;

	public void publish(ProcessOrderMessageDTO processOrderMessageDTO) {
		log.debug("Going to publish the Customer Job Order Message ={}", processOrderMessageDTO);
		this.jmsTemplate.convertAndSend(this.destination, processOrderMessageDTO, (MessagePostProcessor) message -> {
			log.info("In: ProcessOrderMessageDTO=" + processOrderMessageDTO + ", message=" + message);
			message.setStringProperty("messageType", processOrderMessageDTO.getClass().getName());
			message.setDoubleProperty("messageVersion", VERSION);
			return message;
		});
		log.debug("Customer Job Order Message published Successfully for the "
				+ " customer order process Id: {}", processOrderMessageDTO.getCustomerOrderProcessId());
	}

}
