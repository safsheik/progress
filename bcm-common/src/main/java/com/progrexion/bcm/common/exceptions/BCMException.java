package com.progrexion.bcm.common.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BCMException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BCMException(String errorMessage)
	{
		super();
		log.info("Error::"+errorMessage);
	}

	

}
