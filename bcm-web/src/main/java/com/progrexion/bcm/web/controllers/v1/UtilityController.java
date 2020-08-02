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

import com.progrexion.bcm.common.model.v1.UtilityDetailsResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.models.v1.UtilityDetailsResponseDto;
import com.progrexion.bcm.models.v1.UtilityResponseDto;
import com.progrexion.bcm.models.v1.UtilityStatusRequestDto;
import com.progrexion.bcm.services.v1.UtilityService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/customer/utility/{ucid}/{brand}")
public class UtilityController {

	@Autowired
	private UtilityService utilityService;

	@Autowired
	private ModelMapper modelMapper;


	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<UtilityResponseDto[]> getAllUtilities(@PathVariable Long ucid, @PathVariable @NotNull String brand)
			 {
		ResponseEntity<UtilityResponseDto[]> responseEntity;
		log.info("UtilityController: getAllUtilities" + ucid);
		UtilityResponseModel[] response = utilityService.getAllUtilities(ucid, brand);
		if(response.length != 0)
		{
			responseEntity = new ResponseEntity<>(
					modelMapper.map(response, UtilityResponseDto[].class), HttpStatus.OK);
		}
		else {
			responseEntity = new ResponseEntity<>(new UtilityResponseDto[0], HttpStatus.NO_CONTENT);
		}
		return responseEntity;
	}
	
	@RequestMapping(value="/{utilTradeLineId}", method = RequestMethod.GET)
	public ResponseEntity<UtilityDetailsResponseDto[]> getUtilityDetails(@PathVariable Long ucid, @PathVariable @NotNull String brand, @PathVariable Long utilTradeLineId)
			 {

		log.info("UtilityController: getAllUtilities" + ucid);
		ResponseEntity<UtilityDetailsResponseDto[]> responseEntity;
		UtilityDetailsResponseModel[] response = utilityService.getUtilityDetails(ucid, brand,utilTradeLineId);
		if(response.length != 0)
		{
			responseEntity = new ResponseEntity<>(
					modelMapper.map(response, UtilityDetailsResponseDto[].class), HttpStatus.OK);
		}
		else {
			responseEntity = new ResponseEntity<>(new UtilityDetailsResponseDto[0],HttpStatus.NO_CONTENT);
		}
		return responseEntity;


	}
	
	@RequestMapping(method = RequestMethod.PATCH)
	public ResponseEntity<HttpStatus> patchUtiltyStatus(@PathVariable Long ucid, @PathVariable @NotNull String brand,
			@RequestBody @NotNull @Valid UtilityStatusRequestDto utilityStatusRequestDto) {
		log.info("UtilityController: patchUtiltyStatus" + ucid);
		Boolean isUpdated = utilityService.updateUtiltyStatus(ucid, brand, modelMapper.map(utilityStatusRequestDto, UtilityStatusRequestModel.class));

		if (isUpdated) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

	}
	
}
