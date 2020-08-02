package com.progrexion.bcm.advice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.models.v1.ErrorDto;

@RunWith(MockitoJUnitRunner.class)
public class BCMModuleExceptionHandlerTest {

	@InjectMocks
	public BCMModuleExceptionHandler mockExceptionHandler;

	@Test
	public void test_handleInternal() {
		WebRequest request = Mockito.mock(WebRequest.class);
		ResponseEntity<Object> response = mockExceptionHandler.handleInternal(new NullPointerException(), request);
		Assert.assertNotNull(response);
		Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		ErrorDto errorDto = (ErrorDto) response.getBody();
		Assert.assertNotNull(errorDto);
		Assert.assertEquals(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getCode(), errorDto.getErrorCode());
		Assert.assertEquals(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getDescription(), errorDto.getError());
	}
	
	@Test
	public void test_handleBCMModuleException() {
		BCMModuleException exception = new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION);
		ResponseEntity<ErrorDto> response = mockExceptionHandler.handleBCMModuleException(exception);
		Assert.assertNotNull(response);

	}
	
	@Test
	public void test_handleBCMModuleExceptionWithSameErrorData() {
		BCMModuleException exception = new BCMModuleException(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION);
		exception.setErrorMessage(exception.getErrorData());
		ResponseEntity<ErrorDto> response = mockExceptionHandler.handleBCMModuleException(exception);
		Assert.assertNotNull(response);

	}
	
	@Test
	public void test_handleHttpMessageNotReadable() {
		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("[Rent Amount]");
		HttpHeaders headers = HttpHeaders.EMPTY;
		HttpStatus status= HttpStatus.BAD_REQUEST;
		 WebRequest request = Mockito.mock(WebRequest.class);
		ResponseEntity<Object> response = mockExceptionHandler.handleHttpMessageNotReadable(ex, headers, status, request);
		Assert.assertNotNull(response);

	}
	
	@Test
	public void test_handleMethodArgumentNotValid() {
		BindingResult bindingResult =  Mockito.mock(BindingResult.class);
		MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
		HttpHeaders headers = HttpHeaders.EMPTY;
		HttpStatus status= HttpStatus.BAD_REQUEST;
		 WebRequest request = Mockito.mock(WebRequest.class);
		ResponseEntity<Object> response = mockExceptionHandler.handleMethodArgumentNotValid(ex, headers, status, request);
		Assert.assertNotNull(response);

	}

}
