package com.progrexion.bcm.web.controllers.v1;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountResponseModel;
import com.progrexion.bcm.common.model.v1.PlaidReconnectResponseModel;
import com.progrexion.bcm.models.v1.PlaidReconnectResponseDto;
import com.progrexion.bcm.models.v1.PaymentAccountRequestDto;
import com.progrexion.bcm.models.v1.PaymentAccountResponseDto;
import com.progrexion.bcm.services.v1.PlaidService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/v1/customer/payment_account/{ucid}/{brand}")
public class PlaidController {
	@Autowired
	private PlaidService plaidService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	ResponseEntity<?> responseEntity;

	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<PaymentAccountResponseDto> createPaymentAccount(@PathVariable @NotNull long ucid,
			@PathVariable @NotNull String brand, @RequestBody @NotNull @Valid PaymentAccountRequestDto paymentAccountRequestDto) {
		log.info("PlaidController: createPaymentAccount" + ucid);
		PaymentAccountResponseModel paymentAccountResponse = plaidService.createPlaidPaymentAccount(ucid, brand,
				modelMapper.map(paymentAccountRequestDto, PaymentAccountRequestModel.class));
		return new ResponseEntity<>(modelMapper.map(paymentAccountResponse, PaymentAccountResponseDto.class), HttpStatus.CREATED);
	}	

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<PaymentAccountResponseDto> getPaymentAccountDetails(@PathVariable Long ucid,
			@PathVariable @NotNull String brand) {
		log.info("PlaidController: getPaymentAccountDetails: " + ucid);
		ResponseEntity<PaymentAccountResponseDto> responseEntity;
		PaymentAccountResponseModel paymentAccountResponse = plaidService.getPaymentAccountDetails(ucid, brand);
		if (null != paymentAccountResponse)
			responseEntity = new ResponseEntity<>(
					modelMapper.map(paymentAccountResponse, PaymentAccountResponseDto.class), HttpStatus.OK);
		else {
			responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return responseEntity;
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<PlaidReconnectResponseDto> getPublicTokenForReconnect(@PathVariable Long ucid,
			@PathVariable @NotNull String brand) {
		log.info("PlaidController: getPublicTokenForReconnect: " + ucid);
		PlaidReconnectResponseModel response = plaidService.reconnectPlaid(ucid, brand, AppConstants.PLAID_RECONNECTION);
		if (null != response)
			return new ResponseEntity<>(modelMapper.map(response, PlaidReconnectResponseDto.class), HttpStatus.CREATED);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(method = RequestMethod.PATCH)
	public ResponseEntity<PlaidReconnectResponseDto> reconnectToPlaid(@PathVariable @NotNull long ucid,
			@PathVariable @NotNull String brand) {
		log.info("PlaidController: reconnectPlaid" + ucid);
		PlaidReconnectResponseModel response = plaidService.reconnectPlaid(ucid, brand, AppConstants.PLAID_RECONNECTION_PATCH);
		if (response != null)
			return new ResponseEntity<>(modelMapper.map(response, PlaidReconnectResponseDto.class), HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
