package com.progrexion.bcm.web.controllers.v1;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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

import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.CreateUserResponseModel;
import com.progrexion.bcm.common.model.v1.CustomerStatusResponseModel;
import com.progrexion.bcm.models.v1.CreateUserRequestDto;
import com.progrexion.bcm.models.v1.CreateUserResponseDto;
import com.progrexion.bcm.models.v1.CustomerStatusResponseDto;
import com.progrexion.bcm.services.v1.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/customer/{ucid}/{brand}")
public class CustomerController {

	@Autowired
	private CustomerService custService;

	@Autowired
	private ModelMapper modelMapper;

	ResponseEntity<?> responseEntity;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserResponseDto> createCustomerAccount(@PathVariable @NotNull Long ucid,
			@PathVariable @NotNull String brand, @RequestBody @NotNull @Valid CreateUserRequestDto customerDto) {
		log.info("CustomerController: createCustomerAccount", ucid);
		log.info("CustomerController::createCustomerAccount::Timestamp: [{}, {}]", ZonedDateTime.now(), ZoneId.systemDefault());
		CreateUserResponseModel responseModel = custService.createUserAccount(ucid, brand, modelMapper.map(customerDto, CreateUserRequestModel.class));
		return new ResponseEntity<>(modelMapper.map(responseModel, CreateUserResponseDto.class), HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<CustomerStatusResponseDto> getCustomerStatusInfo(@PathVariable Long ucid, @PathVariable @NotNull String brand) {
		log.info("CustomerController: getCustomerStatusInfo UCID: ", ucid);
		CustomerStatusResponseModel customerStatusResponseModel = custService.getCustomerStatusInfo(ucid, brand);
		return new ResponseEntity<>(modelMapper.map(customerStatusResponseModel, CustomerStatusResponseDto.class), HttpStatus.OK);
	}

}