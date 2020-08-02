package com.progrexion.bcm.renttrack.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data

public class MatchTransactionArrayApiModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("transactions")
	private MatchTransactionApiModel[] transactions;


}
