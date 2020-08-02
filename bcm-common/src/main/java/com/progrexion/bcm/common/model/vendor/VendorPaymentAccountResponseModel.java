package com.progrexion.bcm.common.model.vendor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorPaymentAccountResponseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private Long id;
	@JsonProperty("url")
	private String	url;
	@JsonProperty("nickname")
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
	@JsonProperty("needs_reconnect")
	private boolean	needsReconnect;
	@JsonProperty("historical_update_complete")
	private boolean	historicalUpdateComplete;
	@JsonProperty("card_brand")
	private String	cardBrand;

}
