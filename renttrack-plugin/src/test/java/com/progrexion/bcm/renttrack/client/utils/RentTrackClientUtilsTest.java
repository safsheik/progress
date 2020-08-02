package com.progrexion.bcm.renttrack.client.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
@RunWith(MockitoJUnitRunner.class)
public class RentTrackClientUtilsTest {

	@InjectMocks
	private RentTrackClientUtils rtClientUtils;
    private BCMModuleException exception;
    private BCMWebClientResponseException clientException;
    
    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		clientException =
				new BCMWebClientResponseException("[{\"parameter\":\"lease_url\",\"value\":{},\"message\":\"The consumer lease provided already has active transaction finder.\"}]",
						HttpStatus.INTERNAL_SERVER_ERROR, BCMModuleExceptionEnum.RENTTRACK_CLIENT_ERROR);

	}
 @Test
	public void test_getExceptionWithClientException()
	{
	 exception = new BCMModuleException(BCMModuleExceptionEnum.CREATE_TENANT_500);
	 BCMModuleException exceptionResponse = RentTrackClientUtils.getException("CREATE_TENANT_500", clientException);
	 assertNotNull(exceptionResponse);
	 assertEquals(exceptionResponse.getErrorCode(), exception.getErrorCode());
	 assertEquals(exceptionResponse.getHttpStatusCode(), exception.getHttpStatusCode());
	}
 @Test
	public void test_getException()
	{
		exception = new BCMModuleException(BCMModuleExceptionEnum.CREATE_TENANT_BCM_EXCEPTION);
		 BCMModuleException exceptionResponse = RentTrackClientUtils.getException("CREATE_TENANT_BCM_EXCEPTION", exception);
		 assertNotNull(exceptionResponse);
		 assertEquals(exceptionResponse.getErrorCode(), exception.getErrorCode());
		 assertEquals(exceptionResponse.getHttpStatusCode(), exception.getHttpStatusCode());

	}
 
 @Test
	public void test_getExceptionWithClientExceptionwithNull()
	{
	 exception = new BCMModuleException(BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION);
	 BCMModuleException exceptionResponse = RentTrackClientUtils.getException("TEST", clientException);
	 assertNotNull(exceptionResponse);
	 assertEquals(exceptionResponse.getErrorCode(), exception.getErrorCode());
	 assertEquals(exceptionResponse.getHttpStatusCode(), exception.getHttpStatusCode());
	}
 @Test
	public void test_getExceptionwithNull()
	{
		exception = new BCMModuleException(BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION);
		 BCMModuleException exceptionResponse = RentTrackClientUtils.getException("TEST", exception);
		 assertNotNull(exceptionResponse);
		 assertEquals(exceptionResponse.getErrorCode(), exception.getErrorCode());
		 assertEquals(exceptionResponse.getHttpStatusCode(), exception.getHttpStatusCode());

	}

}
