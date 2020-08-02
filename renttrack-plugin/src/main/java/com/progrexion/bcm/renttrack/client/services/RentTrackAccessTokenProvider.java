package com.progrexion.bcm.renttrack.client.services;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.exceptions.BCMWebClientResponseException;
import com.progrexion.bcm.common.model.vendor.VendorTokenResponseModel;
import com.progrexion.bcm.common.model.vendor.enums.RentTrackClientRequestTypesEnum;
import com.progrexion.bcm.common.processor.VendorProcessor;
import com.progrexion.bcm.common.properties.RentTrackAPIOAuthConfigProperties;
import com.progrexion.bcm.common.utils.CommonUtils;
import com.progrexion.bcm.common.webclients.errorhandlers.WebClientErrorHandler;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.repositories.CustomerRepository;
import com.progrexion.bcm.renttrack.client.model.RTAccessTokenResponseModel;
import com.progrexion.bcm.renttrack.client.utils.RentTrackClientUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Qualifier("rtTokenProvider")
@Slf4j

public class RentTrackAccessTokenProvider implements AccessTokenProviderService {
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	@Qualifier("renttrackprocessor")
	private VendorProcessor renttrackprocessor;
	
	@Autowired
	private WebClientErrorHandler webClientErrorHandler;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RentTrackAPIOAuthConfigProperties rtPartnerProperties;
	

	@Autowired
	private WebClient rentTrackWebClient;
	
	@Override
	public VendorTokenResponseModel getCurrentTokensOfUser(Long ucid,String brand) {
		BCMCustomer bcmCustomer = customerRepo.findByUcIdAndBrand(ucid, brand).orElseThrow();
		VendorTokenResponseModel tokens = new VendorTokenResponseModel();
		tokens.setAccessToken(bcmCustomer.getAccessToken());
		tokens.setRefreshToken(bcmCustomer.getRefreshToken());
		return tokens;
	}
	
	@Override
	public VendorTokenResponseModel refreshTokensOfUser(Long ucid,String brand,String reqType) {
		BCMCustomer bcmCustomer = customerRepo.findByUcIdAndBrand(ucid, brand).orElseThrow();
		VendorTokenResponseModel newTokens = modelMapper.map(getAccessTokenFromRefreshToken(bcmCustomer.getRefreshToken(),reqType), VendorTokenResponseModel.class);
		bcmCustomer.setAccessToken(newTokens.getAccessToken());
		bcmCustomer.setRefreshToken(newTokens.getRefreshToken());
		bcmCustomer.setTokenExpiry(newTokens.getExpiresIn());
		bcmCustomer.setTokenCreatedDate();
		bcmCustomer.setModifiedDate();
		customerRepo.save(bcmCustomer);
		return newTokens;
	}
	
	public String getPartnerAccessToken() {
		return this.getPartnerTokens().getAccessToken();
	}
	
	@Override
	public String getCurrentAccessTokenOfUser(Long ucid,String brand) {
		return this.getCurrentTokensOfUser(ucid,brand).getAccessToken();
	}
	
	@Override
	public String refreshAndGetAccessTokenOfUser(Long ucid,String brand) {
		return this.refreshTokensOfUser(ucid,brand,null).getAccessToken();
	}
	
	@Override
	public VendorTokenResponseModel getFirstTimeUserTokens(String username, String secret) {
		String response = null;
		try {
			rentTrackWebClient = WebClient.builder()
					.filter(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter())
					.baseUrl(rtPartnerProperties.getBaseUrl())
					.build();
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(getTokenApi(username,secret))
					.exchange().block().bodyToMono(String.class).block();

			return modelMapper.map(objectMapper.readValue(response, RTAccessTokenResponseModel.class), VendorTokenResponseModel.class);

		} catch (BCMWebClientResponseException wcEx) {
			log.error("BCMModuleException while getting RentTrack UserToken, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			BCMModuleException e = RentTrackClientUtils.getException("ACCESS_TOKEN_"+wcEx.getHttpStatus().value(), wcEx);
			throw new BCMModuleException(e.getErrorCode(),e.getErrorMessage(),e.getHttpStatusCode());
		} 
		catch(Exception ex)
		{
			log.error("BCMModuleException while getting RentTrack UserToken, ErrorMessage: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.ACCESS_TOKEN_BCM_EXCEPTION);
		}
	}
	
	@Override
	public VendorTokenResponseModel getPartnerTokens() {
		String response = null;
		
		try {
			rentTrackWebClient = WebClient.builder()
					.filter(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter())
					.baseUrl(rtPartnerProperties.getBaseUrl())
					.build();
			
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(getTokenApi(rtPartnerProperties.getUsername(),rtPartnerProperties.getPassword()))
					.exchange().block().bodyToMono(String.class).block();

			log.info("getPartnerAccessToken::response:[{}]", response);

			return objectMapper.readValue(response, VendorTokenResponseModel.class);

		} catch (BCMWebClientResponseException wcEx) {
			log.error("BCMModuleException while getting RentTrack PartnerToken, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			BCMModuleException e = RentTrackClientUtils.getException("PARTNER_TOKEN_"+wcEx.getHttpStatus().value(), wcEx);
			throw new BCMModuleException(e.getErrorCode(),e.getErrorMessage(),e.getHttpStatusCode());
		} 
		catch(Exception ex)
		{
			log.error("BCMModuleException while getting RentTrack PartnerToken: {}", ex.getMessage(), ex);
			throw new BCMModuleException(BCMModuleExceptionEnum.PARTNER_TOKEN_BCM_EXCEPTION);
		}
	}
	
	private RTAccessTokenResponseModel getAccessTokenFromRefreshToken(String refreshToken,String reqType){
		String response = null;

		try {
			rentTrackWebClient = WebClient.builder()
					.filter(webClientErrorHandler.rentTrackWebClientErrorHandlingFilter())
					.baseUrl(rtPartnerProperties.getBaseUrl())
					.build();
			response = rentTrackWebClient.method(HttpMethod.GET)
					.uri(getRefreshTokenApi(refreshToken))
					.exchange().block().bodyToMono(String.class).block();

			return objectMapper.readValue(response, RTAccessTokenResponseModel.class);

		} catch (BCMWebClientResponseException wcEx) {
			log.error("BCMModuleException while getting RentTrack AccessToken from RefreshToken, ErrorMessage: {}",
					wcEx.getErrorMessage(), wcEx);
			BCMModuleException e = RentTrackClientUtils.getException(reqType+"_REFRESH_TOKEN_"+wcEx.getHttpStatus().value(), wcEx);
			throw new BCMModuleException(e.getErrorCode(),e.getErrorMessage(),e.getHttpStatusCode());
		}
		catch (Exception e) {
			BCMModuleExceptionEnum exceptionEnum = BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION;
			if(StringUtils.isNotEmpty(reqType))
			{
				exceptionEnum = CommonUtils.getExceptionEnum(reqType+"_REFRESH_TOKEN_BCM_EXCEPTION");
			}
			
			throw new BCMModuleException(exceptionEnum);

		}
	}
	
	@Override
	public VendorTokenResponseModel getCurrentTokensOfUser(Long customerId) {
		BCMCustomer bcmCustomer = customerRepo.findByCustomerDataId(customerId).orElseThrow();
		VendorTokenResponseModel tokens = new VendorTokenResponseModel();
		tokens.setAccessToken(bcmCustomer.getAccessToken());
		tokens.setRefreshToken(bcmCustomer.getRefreshToken());
		return tokens;
	}

	@Override
	public String getCurrentAccessTokenOfUser(Long customerId) {
		return this.getCurrentTokensOfUser(customerId).getAccessToken();
	}

	@Override
	public VendorTokenResponseModel refreshTokensOfUser(Long customerId, RentTrackClientRequestTypesEnum reqType)
	 {
			BCMCustomer bcmCustomer = customerRepo.findByCustomerDataId(customerId).orElseThrow();
			VendorTokenResponseModel newTokens = modelMapper.map(getAccessTokenFromRefreshToken(bcmCustomer.getRefreshToken(),reqType.name()), VendorTokenResponseModel.class);
			bcmCustomer.setAccessToken(newTokens.getAccessToken());
			bcmCustomer.setRefreshToken(newTokens.getRefreshToken());
			bcmCustomer.setTokenExpiry(newTokens.getExpiresIn());
			bcmCustomer.setTokenCreatedDate();
			bcmCustomer.setModifiedDate();
			customerRepo.save(bcmCustomer);
			return newTokens;
	}
	
	private String getTokenApi(String userName,String userSecret)
	{
		String api = rtPartnerProperties.getTokenUrl();
		api = api.replace(AppConstants.CLIENT_ID,rtPartnerProperties.getClientId())
				.replace(AppConstants.CLIENT_SECRET,rtPartnerProperties.getClientSecret())
				.replace(AppConstants.USER_NAME, userName)
				.replace(AppConstants.SECRET, userSecret)
				.replace(AppConstants.GRANT_TYPE,rtPartnerProperties.getGrantType());
		return api;
	}
	
	private String getRefreshTokenApi(String refreshToken)
	{
		String api = rtPartnerProperties.getRefreshTokenUrl();
		api = api.replace(AppConstants.CLIENT_ID,rtPartnerProperties.getClientId())
				.replace(AppConstants.CLIENT_SECRET,rtPartnerProperties.getClientSecret())
				.replace(AppConstants.REFRESH_TOKEN,refreshToken)
				.replace(AppConstants.GRANT_TYPE,AppConstants.REFRESH_TOKEN_GRAND_TYPE);
		return api;
	}
	
	


}
