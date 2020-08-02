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

import com.progrexion.bcm.common.model.v1.LeaseOrdersResponseModel;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.models.v1.LeaseDto;
import com.progrexion.bcm.models.v1.LeaseOrdersResponseDto;
import com.progrexion.bcm.models.v1.LeaseRequestDto;
import com.progrexion.bcm.models.v1.LeaseResponseDto;
import com.progrexion.bcm.services.v1.LeaseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/customer/lease")
public class LeaseController {

	@Autowired
	private LeaseService leaseService;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping(value = "/{ucid}/{brand}", method = RequestMethod.GET)
	public ResponseEntity<LeaseDto> getLeaseInfo(@PathVariable Long ucid, @PathVariable @NotNull String brand) {
		log.info("LeaseController: Get Lease data: " + ucid);
		LeaseResponseModel leaseResponseModel = leaseService.getLeaseInfo(ucid, brand);
		return new ResponseEntity<>(modelMapper.map(leaseResponseModel, LeaseDto.class), HttpStatus.OK);
	}

	@RequestMapping(value = "/{ucid}/{brand}", method = RequestMethod.POST)
	public ResponseEntity<LeaseResponseDto> createLease(@PathVariable Long ucid, @PathVariable @NotNull String brand,
			@RequestBody @NotNull @Valid LeaseRequestDto leaseRequestDto) {
		log.info("LeaseController: Create Lease: " + ucid);
		boolean isSubscribed = true;
		LeaseResponseModel leaseResponseModel = leaseService.createOrUpdateLease(ucid, brand, modelMapper.map(leaseRequestDto, LeaseRequestModel.class), isSubscribed);
		return new ResponseEntity<>(modelMapper.map(leaseResponseModel, LeaseResponseDto.class), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/orders/{ucid}/{brand}", method = RequestMethod.GET)
	public ResponseEntity<LeaseOrdersResponseDto[]> getLeaseOrders(@PathVariable Long ucid,
			@PathVariable @NotNull String brand) {
		log.info("LeaseController: get Lease Orders : " + ucid);
		ResponseEntity<LeaseOrdersResponseDto[]> responseEntity;
		LeaseOrdersResponseModel[] leaseOrderResponse = leaseService.getLeaseOrders(ucid, brand);
		if (0 != leaseOrderResponse.length) {
			responseEntity = new ResponseEntity<>(modelMapper.map(leaseOrderResponse, LeaseOrdersResponseDto[].class), HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return responseEntity;
	}

}
