package com.progrexion.bcm.services.persistence;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.progrexion.bcm.common.enums.SubscriptionStatusEnum;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.properties.BCMConfigProperties;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.ExternalLog;
import com.progrexion.bcm.model.entities.WebServiceLog;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.model.repositories.ExternalLogsRepository;
import com.progrexion.bcm.model.repositories.WebServiceLogRepository;
import com.progrexion.bcm.services.util.TestServiceUtils;


@RunWith(MockitoJUnitRunner.class)
public class PersistenceServiceImplTest {


	@InjectMocks
	private PersistenceServiceImpl persistenceService;
	

	@Mock
	private WebServiceLogRepository webServiceLogRepository;
	
	@Mock
	private ExternalLogsRepository externalLogRepository;

	@Mock
	private CustomerRepository customerRepository;
	@Mock
	private BCMConfigProperties property;
	
	@Before
	public void setProperties()
	{
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void test_saveWebServiceLogs() {
		WebServiceLog webServiceLog = buildWebServiceLog();
		Mockito.when(webServiceLogRepository.save(Mockito.any(WebServiceLog.class))).thenReturn(webServiceLog);
		persistenceService.saveWebServiceLogs(webServiceLog);
		verify(webServiceLogRepository, times(1)).save(Mockito.any());
	}

	@Test(expected = BCMModuleException.class)
	public void test_saveWebServiceLogsWithException() {
		WebServiceLog webServiceLog = buildWebServiceLog();
		Mockito.when(webServiceLogRepository.save(Mockito.any(WebServiceLog.class)))
				.thenThrow(new NullPointerException("Exception occurs"));
		persistenceService.saveWebServiceLogs(webServiceLog);
	}

	
	@Test
	public void test_saveExternalLogs() {
		ExternalLog externalLog = buildExternalLog();
		Mockito.when(externalLogRepository.save(Mockito.any(ExternalLog.class))).thenReturn(externalLog);
		persistenceService.saveExternalLogs(externalLog);
		verify(externalLogRepository, times(1)).save(Mockito.any());
	}

	@Test(expected = BCMModuleException.class)
	public void test_saveExternalLogsException() {
		ExternalLog externalLog = new ExternalLog();
		Mockito.when(externalLogRepository.save(Mockito.any(ExternalLog.class)))
				.thenThrow(new NullPointerException("Exception occurs"));
		persistenceService.saveExternalLogs(externalLog);
	}


	private WebServiceLog buildWebServiceLog() {
		WebServiceLog webServiceLog = new WebServiceLog();
		webServiceLog.setUcid(101l);
		webServiceLog.setHttpMethod("POST");
		webServiceLog.setHttpStatusCode(200l);
		webServiceLog.setRequestPayload("reqeust");
		webServiceLog.setResponsePayload("response");
		webServiceLog.setUrl("customer/v1/1001");
		webServiceLog.setBrand("CCOM");
		return webServiceLog;
	}


	public ExternalLog buildExternalLog() {
		ExternalLog externalLog = new ExternalLog();
		externalLog.setUcid(101l);
		externalLog.setBrand("CCOM");
		externalLog.setHttpMethod("POST");
		externalLog.setVendorId(1l);
		externalLog.setUrl("vendorurl");
		externalLog.setVendorRequestPayload("");
		externalLog.setVendorResponsePayload("");
		externalLog.setCustomerId(1l);
		return externalLog;
	}
	@Test
	public void test_getLatestActiveCustomer() {
		Optional<BCMCustomer> customerOpt = Optional.of(TestServiceUtils.getCustomerObject());
		Set<Long> customerAllUcids = new HashSet<>();
		customerAllUcids.add(101l);
		Mockito.when(customerRepository.findTop1ByUcIdInAndBrandOrderByCustomerDataIdDesc
				(Mockito.any(),Mockito.any())).thenReturn(customerOpt);
		assertNotNull(persistenceService.getLatestActiveCustomer(customerAllUcids,"CCOM"));
	}
	
	@Test
	public void test_getNewAndReportExpiredCustomers() {
		List<BCMCustomer> customerList = new ArrayList<>();
		Mockito.when(customerRepository.findAllByBrandAndTransactionPullDate
				(Mockito.any(),Mockito.anyString(),Mockito.anyInt(),Mockito.any())).thenReturn(customerList);
		assertNotNull(persistenceService.getNewAndReportExpiredCustomers(ZonedDateTime.now(),"CCOM"));
	}
	
	@Test
	public void test_getCustomerStatustInfo() {
		List<SubscriptionStatusEnum> subEnumList = new ArrayList<>();
		subEnumList.add(SubscriptionStatusEnum.ACTIVE);
		Page<SubscriptionStatusEnum> page = new PageImpl<>(subEnumList);
		when(customerRepository.findCustomerSubscriptionStatusInfo
				(Mockito.anyLong(),Mockito.anyString(),Mockito.anyInt(),Mockito.any())).thenReturn(page);
		assertNotNull(persistenceService.getCustomerStatustInfo(1l, "CCOM"));
	}
}
