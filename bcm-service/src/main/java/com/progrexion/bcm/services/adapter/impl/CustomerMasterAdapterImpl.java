package com.progrexion.bcm.services.adapter.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
import com.progrexion.bcm.common.properties.CustomerMasterConfigProperties;
import com.progrexion.bcm.common.utils.BCMExceptionUtils;
import com.progrexion.bcm.common.webclients.utils.WebClientUtils;
import com.progrexion.bcm.services.adapter.CustomerMasterAdapter;
import com.progrexion.customermaster.ws.v4.model.Customer;
import com.progrexion.customermaster.ws.v4.model.MergeID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerMasterAdapterImpl implements CustomerMasterAdapter {
	@Autowired
	private CustomerMasterConfigProperties property;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private WebClient customerMasterWebClient;
	public static final String CUSTOMER_MASTER = "CUSTOMER_MASTER";
	
	@Override
	public Set<Long> getCustomerUCIDs(Long ucid) {

		Customer customer = getCustomer(ucid);
		Set<Long> customerAllUcids = new HashSet<>();
		if(customer !=null)
		{
			customerAllUcids.add(customer.getUcid());
			Set<MergeID> mergedIds=customer.getMergeIDs();
			if(mergedIds != null)
			{
				for(MergeID id: mergedIds) {
					customerAllUcids.add(id.getUcid());
				}
			}
		}
		else
		{
			log.info("Data not available in customer Master for the UCID [{}}",ucid);
			//customerAllUcids.add(ucid);
		}
		
		return customerAllUcids;

	}
	
	@Override
	public Long getCustomerParentUCID(Long ucid) {

		Customer customer = getCustomer(ucid);
		if(customer !=null)
		{
			return customer.getUcid();
		}
		else
		{
			throw new BCMModuleException(BCMModuleExceptionEnum.CUSTOMER_INFO_NOT_FOUND);
		}
		
	}

	private Customer getCustomer(Long ucid) {

		String response = null;
		Customer customer = null;

		try {
			response = this.customerMasterWebClient.method(HttpMethod.GET)
					.uri(uribuilber -> uribuilber
							.path(property.getCustomerUri())
							.build(Map.of(AppConstants.UCID, ucid)))
					.exchange()
					.retry(property.getApiRetryCount(),
							e -> WebClientUtils.isRetryRequired(e, CustomerMasterAdapterImpl.class.getName(),
									"getCustomer(Long ucid) ", ucid))
					.block().bodyToMono(String.class).block();
			if(StringUtils.isNotBlank(response))
			{
				customer = objectMapper.readValue(response, Customer.class);
			}
			return customer;

		} catch (BCMWebClientResponseException wcEx) {
			log.error("BCMWebClientResponseException while getting mergedIDS, ErrorMessage: {}", wcEx.getErrorMessage(),
					wcEx);
//			throw new BCMModuleException(
//					wcEx.getBcmModuleExceptionEnum().getDescription() + ":" + wcEx.getErrorMessage(),
//					wcEx.getBcmModuleExceptionEnum());
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(CUSTOMER_MASTER, wcEx);
		} catch (Exception ex) {

			log.error("Exception while getting customer, ErrorMessage: {}", ex.getMessage(), ex);
//			throw new BCMModuleException(
//					"Exception while getting customer from Customer Master" + ":"
//							+ ExceptionUtils.getRootCauseMessage(ex),
//							BCMModuleExceptionEnum.REMOTE_SYSTEM_EXCEPTION);
			throw BCMExceptionUtils.getBCMModuleExceptionMessage(CUSTOMER_MASTER, ex);
		}
	}

}
