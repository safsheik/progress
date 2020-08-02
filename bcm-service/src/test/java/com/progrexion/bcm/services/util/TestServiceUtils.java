package com.progrexion.bcm.services.util;

import java.time.LocalDate;
import java.util.List;

import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.LeaseResponseModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.v1.PaymentAccountRequestModel;
import com.progrexion.bcm.common.model.v1.SubscriptionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionModel;
import com.progrexion.bcm.common.model.v1.TransactionRequestModel;
import com.progrexion.bcm.common.model.v1.TransactionResponseModel;
import com.progrexion.bcm.common.model.v1.UtilityStatusRequestModel;
import com.progrexion.bcm.common.model.vendor.LeaseAddress;
import com.progrexion.bcm.common.model.vendor.LeaseLandlord;
import com.progrexion.bcm.common.model.vendor.VendorLeaseResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorSearchTransactionResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.services.constants.TestConstants;

public class TestServiceUtils {

	
	public static BCMCustomer getCustomerObject() {

		BCMCustomer bCMCustomer = new BCMCustomer();
		
	    bCMCustomer.setUcId(TestConstants.UCID);
	    bCMCustomer.setBrand(TestConstants.BRAND_CCOM);
	    bCMCustomer.setAccessToken(TestConstants.ACCESS_TOKEN);
	    bCMCustomer.setRefreshToken(TestConstants.REFRESH_TOKEN);
	    bCMCustomer.setCustEmail(TestConstants.EMAIL);
	    bCMCustomer.setTransactionFinderId(123123l);
	    bCMCustomer.setCustomerDataId(101l);
	    bCMCustomer.setResidentStatus(1);
	    bCMCustomer.setPaymentAccountId(1l);
		return bCMCustomer;

	}

	public static CreateUserRequestModel getCreateUserObject() {

		CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();

		createUserRequestModel.setEmail(TestConstants.EMAIL);
		createUserRequestModel.setType(TestConstants.USER_TYPE);
		createUserRequestModel.setFirstName(TestConstants.FIRST_NAME);
		createUserRequestModel.setLastName(TestConstants.LAST_NAME);
		createUserRequestModel.setDob(LocalDate.parse("1992-01-01"));

		return createUserRequestModel;

	}

	public static LeaseRequestModel getLeaseObject() {

		LeaseRequestModel leaseReqModel = new LeaseRequestModel();
		Address address = new Address("Plot A5", "STREET", "CITY", "4900051", "STATE", "US");
		Landlord landlord = new Landlord("", "John", "7894561234", "abc@test.com");
		leaseReqModel.setAddress(address);
		leaseReqModel.setDueDay(1);
		leaseReqModel.setLandlord(landlord);
		leaseReqModel.setRent((float) 500.00);
		return leaseReqModel;

	}
	
	public static LeaseResponseModel getLeaseResponseModelObject() {

		LeaseResponseModel leaseResponseModel = new LeaseResponseModel();
		Address address = new Address("Plot A5", "STREET", "CITY", "4900051", "STATE", "US");
		Landlord landlord = new Landlord("", "John", "7894561234", "abc@test.com");
		leaseResponseModel.setLeaseId(1001l);
		leaseResponseModel.setAddress(address);
		leaseResponseModel.setDueDay("1");
		leaseResponseModel.setLandlordModel(landlord);
		leaseResponseModel.setRent("500.00");
		return leaseResponseModel;

	}
	
	public static VendorLeaseResponseModel getVendorLeaseResponseModelObject() {

		VendorLeaseResponseModel responseModel = new VendorLeaseResponseModel();
		LeaseAddress address = new LeaseAddress("Plot A5", "STREET", "CITY", "4900051", "STATE", "US");
		LeaseLandlord landlord = new LeaseLandlord("", "John", "7894561234", "abc@test.com");
		responseModel.setLeaseId(1001l);
		responseModel.setAddress(address);
		responseModel.setDueDay("1");
		responseModel.setLandlord(landlord);
		responseModel.setRent(500.00f);
		return responseModel;

	}
	public static MatchTransactionRequestModel getMatchTransactionRequestModelObj() {

		MatchTransactionRequestModel matchTransactionRequestModel = new MatchTransactionRequestModel();

		matchTransactionRequestModel.setTransactionID("ojEBr8gApyspayb3EeZ1Uy8dNmyRz6FRe6E6m");
		matchTransactionRequestModel.setAmount("6.33");
		matchTransactionRequestModel.setName("Uber 072515 SF**POOL**");
		matchTransactionRequestModel.setCategoryId("22006001");
		matchTransactionRequestModel.setDate(LocalDate.parse("2019-10-15"));

		return matchTransactionRequestModel;

	}
	
	public static PaymentAccountRequestModel getPaymentAccountRequestModelObj() {

		PaymentAccountRequestModel  reqModel =  new PaymentAccountRequestModel();
        reqModel.setPublicToken("public-sandbox-34345");
        reqModel.setPlaidAccountId("asd3465");
		return reqModel;

	}
	
	public static VendorPaymentAccountResponseModel getVendorPaymentAccountResponseModelObj() {

		VendorPaymentAccountResponseModel  resModel =  new VendorPaymentAccountResponseModel();
		resModel.setId(101l);
		resModel.setPlaidAccount(true);
		return resModel;

	}
	
	public static TransactionRequestModel getTransactionRequestModelObj() {

		TransactionRequestModel  reqModel =  new TransactionRequestModel();
		reqModel.setDueDay(4);
		reqModel.setRentAmount(50f);
		return reqModel;

	}
	public static VendorTransactionResponseModel getVendorTransactionResponseModelObj() {

		VendorTransactionResponseModel  resModel =  new VendorTransactionResponseModel();
		resModel.setId(101l);
		resModel.setStatus("pending");
		return resModel;

	}
	
	public static VendorSearchTransactionResponseModel getVendorSearchTransactionResponseModelObj() {

		VendorSearchTransactionResponseModel  resModel =  new VendorSearchTransactionResponseModel();
		resModel.setCategoryId("12213l");
		resModel.setTransactionId("134YNKLSDHAMDSD");
		return resModel;

	}
	
	public static VendorMatchTransactionResponseModel getVendorMatchTransactionResponseModelObj() {

		VendorMatchTransactionResponseModel resModel = new VendorMatchTransactionResponseModel();

		resModel.setId(101l);
		resModel.setPaidFor("GAS");
		return resModel;

	}
	
	public static SubscriptionRequestModel getSubscriptionRequestModelObj() {

		SubscriptionRequestModel reqModel = new SubscriptionRequestModel();
		reqModel.setPromotionCode("CREDITCOMFREE");
		return reqModel;

	}
	
	public static UtilityStatusRequestModel getUtilityStatusRequestModelObj() {

		UtilityStatusRequestModel utilityStatusRequestModel = new UtilityStatusRequestModel();
		utilityStatusRequestModel.setStatus("current");
		utilityStatusRequestModel.setOptFor("GAS");
		return utilityStatusRequestModel;
	}
	
	public static TransactionRequestModel getPlaidPaymentAccountReqObj() {

		TransactionRequestModel transactionRequestModel = new TransactionRequestModel();
		transactionRequestModel.setDueDay(10);
		transactionRequestModel.setLeaseId(12120L);
		transactionRequestModel.setRentAmount(100.00f);
				
		return transactionRequestModel;
	}
	
	public static TransactionResponseModel getPlaidPaymentAccountObj() {

		TransactionResponseModel transactionResponseModel = new TransactionResponseModel();
		List<TransactionModel> transactionModel = null;
		transactionResponseModel.setStatus("Found");
		transactionResponseModel.setTransactions(transactionModel);
				
		return transactionResponseModel;
	}
	
	public static VendorPaymentAccountResponseModel getPlaidPaymentAccountRequestObj() {
		VendorPaymentAccountResponseModel transactionResponseModel = new VendorPaymentAccountResponseModel();
		transactionResponseModel.setId(101L);;
		transactionResponseModel.setCardBrand("CCOM");
		transactionResponseModel.setInstitutionId("10120");
				
		return transactionResponseModel;
	}



}
