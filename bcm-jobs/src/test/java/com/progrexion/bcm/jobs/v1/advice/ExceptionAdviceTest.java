package com.progrexion.bcm.jobs.v1.advice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.models.v1.ErrorDto;



@RunWith(MockitoJUnitRunner.class)
public class ExceptionAdviceTest {

	@InjectMocks
	public ExceptionAdvice mockExceptionAdvice;

	@Test
	public void test_handleInternal() {
		ResponseEntity<?> response = mockExceptionAdvice.handlePlatformException(new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION));
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		ErrorDto errorDto = (ErrorDto) response.getBody();
		Assert.assertNotNull(errorDto);
	}
}
