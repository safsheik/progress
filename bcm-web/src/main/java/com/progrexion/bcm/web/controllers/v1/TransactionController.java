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

import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionResponseModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.models.v1.MatchTransactionRequestDto;
import com.progrexion.bcm.models.v1.MatchTransactionResponseDto;
import com.progrexion.bcm.models.v1.TransactionRequestDto;
import com.progrexion.bcm.models.v1.TransactionResponseDto;
import com.progrexion.bcm.services.v1.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/customer/transaction")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping(value = "/{ucid}/{brand}", method = RequestMethod.GET)
	public ResponseEntity<TransactionResponseDto> getTransactionDetails(@PathVariable Long ucid,
			@PathVariable @NotNull String brand) {
		log.info("TransactionController: Search Transaction Details: " + ucid);
		TransactionResponseModel transactionResponseModel = transactionService.searchTransactionDetails(ucid, brand);
		return new ResponseEntity<>(modelMapper.map(transactionResponseModel, TransactionResponseDto.class), HttpStatus.OK);
	}

	@RequestMapping(value = "/{ucid}/{brand}", method = RequestMethod.POST)
	public ResponseEntity<TransactionResponseDto> createTransactionFinder(@PathVariable Long ucid,
			@PathVariable @NotNull String brand, @RequestBody @NotNull @Valid TransactionRequestDto transactionRequestDto) {
		log.info("TransactionController: Create a transaction finder: " + ucid);
		TransactionResponseModel transactionResponseModel = transactionService.createTransactionFinder(ucid, brand, modelMapper.map(transactionRequestDto, TransactionRequestModel.class));
		return new ResponseEntity<>(modelMapper.map(transactionResponseModel, TransactionResponseDto.class), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/match/{ucid}/{brand}", method = RequestMethod.POST)
	public ResponseEntity<MatchTransactionResponseDto> matchTransaction(@PathVariable @NotNull long ucid,
			@PathVariable @NotNull String brand, @RequestBody @NotNull MatchTransactionRequestDto matchTransactionRequestDto) {
		log.info("TransactionController: Match Transaction: " + ucid);
		MatchTransactionResponseModel response = transactionService.matchTransaction(ucid, brand,
				modelMapper.map(matchTransactionRequestDto, MatchTransactionRequestModel.class));
		return new ResponseEntity<>(modelMapper.map(response, MatchTransactionResponseDto.class), HttpStatus.CREATED);
	}
	
}
