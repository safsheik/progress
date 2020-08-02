package com.progrexion.bcm.common;

public class AppConstants {

	private AppConstants() {
		
	}
	public static final String UCID = "UCID";
	public static final String BRAND = "BRAND";
	public static final String COMMISSION_AMOUNT = "commission_amount";
	public static final String SERVICELEVEL = "servicelevel";
	public static final String ALERT_TYPE = "alert_type";
	public static final String SOURCE_SYSTEM = "source_system";
	public static final String DESCRIPTION = "description";
	
	public static final String DEFAULT_RENTTRACK_USER_ACCOUNT_TYPE = "tenant";
	public static final String DEFAULT_PREFIX = "Pgx@";
	
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	public static final String HTTP_METHOD_PUT = "PUT";
	public static final String HTTP_METHOD_DELETE = "DELETE";
	public static final String HTTP_METHOD_PATCH = "PATCH";
	
	public static final String CLIENT_ID = "CLIENT_ID";
	public static final String CLIENT_SECRET = "CLIENT_SECRET";
	public static final String USER_NAME = "USER_NAME";
	public static final String SECRET = "SECRET";
	public static final String GRANT_TYPE = "GRANT_TYPE";
	public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
	public static final String REFRESH_TOKEN_GRAND_TYPE = "refresh_token";
	public static final String REFRESH_TOKEN_EXPIRY_MSG = "The access token provided has expired";	
	public static final String LEASE_ID = "LEASE_ID";
	public static final String TRX_FINDER_ID = "TRX_FINDER_ID";
	public static final String SEARCH_TRX_ID = "SEARCH_TRX_ID";
	public static final String GET_LEASE = "GET_LEASE";
	public static final String UNDERSCORE = "_";
	public static final boolean TRUE = true;
	public static final boolean FALSE = false;
	public static final String PAYMENT_ACCOUNT_ID = "PAYMENT_ACCOUNT_ID";
	public static final String PLAID_RECONNECTION = "PLAID_RECONNECTION";
	public static final String PLAID_RECONNECTION_PATCH = "PLAID_RECONNECTION_PATCH";
	public static final String DISABLED = "Disabled";
	public static final String ON = "ON";
	public static final String OFF = "OFF";
	public static final String UTILITY_ID = "UTILITY_ID";
	public static final String WEBCLIENT_MSG = "\"message\"";
	public static final String WEBCLIENT_ERROR_DESC = "\"error_description\"";
	public static final String WEBCLIENT_MSG_REGEX = "[ ]?=[ ]|[^-_,A-Za-z0-9 :\\\"]+";
	public static final String TRANSACTION_STATUS_FOUND = "FOUND";
	public static final String TRANSACTION_STATUS_MULTIPLE= "MULTIPLE";
	public static final String LEASE_STATUS_ACTIVE = "active";
	public static final String NOT_CREATED= "NOT CREATED";
}
