package com.progrexion.bcm.renttrack.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RTAccessTokenResponseModel {

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("expires_in")
	private Long expiresIn;

	private String scope;

	/*
	 * public String getAccessToken() { return accessToken; }
	 * 
	 * public void setAccessToken(String accessToken) { this.accessToken =
	 * accessToken; }
	 * 
	 * public String getRefreshToken() { return refreshToken; }
	 * 
	 * public void setRefreshToken(String refreshToken) { this.refreshToken =
	 * refreshToken; }
	 * 
	 * public String getTokenType() { return tokenType; }
	 * 
	 * public void setTokenType(String tokenType) { this.tokenType = tokenType; }
	 * 
	 * public Long getExpiresIn() { return expiresIn; }
	 * 
	 * public void setExpiresIn(Long expiresIn) { this.expiresIn = expiresIn; }
	 * 
	 * public String getScope() { return scope; }
	 * 
	 * public void setScope(String scope) { this.scope = scope; }
	 * 
	 * @Override public String toString() { return
	 * "RTAccessTokenResponseModel [accessToken=" + accessToken + ", refreshToken="
	 * + refreshToken + ", tokenType=" + tokenType + ", expiresIn=" + expiresIn +
	 * ", scope=" + scope + "]"; }
	 */

}
