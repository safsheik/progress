package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
  public class PaymentAccountResponseDto implements Serializable {
  private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private Long id;
	@JsonProperty("nick_name")
	private String	nickName;
	@JsonProperty("type")
	private String	type;
	@JsonProperty("last_four")
	private String	lastFour;
	@JsonProperty("expiration")
	private String	expiration;
	@JsonProperty("institution_id")
	private String	institutionId;
	@JsonProperty("plaid_account")
	private boolean	plaidAccount;
	@JsonProperty("reconnect_to_plaid")
	private boolean	needsReconnect;
	@JsonProperty("historical_update_complete")
	private boolean	historicalUpdateComplete;
	@JsonProperty("card_brand")
	private String	cardBrand;
	
	private String status;
	@JsonProperty("transactions")
	private List<TransactionDto> transactions;

}
