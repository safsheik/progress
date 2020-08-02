package com.progrexion.bcm.messaging.listeners;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public abstract class BaseListener {
	@Autowired protected ObjectMapper objectMapper;
	@Autowired protected ModelMapper modelMapper;
		
	protected <R> R convert(Message t, Class<R> r) {
		try {
			return objectMapper.readValue(((TextMessage) t).getText(), r);
		} catch (Exception e) {
			log.error("Unable to parse the posted message :  {}", e.getMessage());
			throw new BCMModuleException(BCMModuleExceptionEnum.UNABLE_TO_PARSE_MESSAGE_BCM_MESSAGING_EXCEPTION);
		}
	}
	
}
