package com.progrexion.bcm.services.constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.progrexion.bcm.common.jobs.enums.JobEventTypeEnum;
import com.progrexion.bcm.common.jobs.enums.JobNameEnum;
import com.progrexion.bcm.common.model.v1.Address;
import com.progrexion.bcm.common.model.v1.CreateUserRequestModel;
import com.progrexion.bcm.common.model.v1.Landlord;
import com.progrexion.bcm.common.model.v1.LeaseRequestModel;
import com.progrexion.bcm.common.model.v1.MatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersReportedResponseModel;
import com.progrexion.bcm.common.model.vendor.VendorOrdersResponseModel;
import com.progrexion.bcm.model.entities.BCMCustomer;
import com.progrexion.bcm.model.entities.Job;
import com.progrexion.bcm.model.entities.JobEvent;
import com.progrexion.bcm.model.entities.Order;
import com.progrexion.bcm.model.entities.OrderReportedInfo;

public class TestConstants {

	public static final long UCID = (long) 101;
	public static final String MESSAGE_BODY = "Message Body";

	public static final String BRAND_CCOM = "CCOM";


	public static final String FIRST_NAME = "Fname";
	public static final String LAST_NAME = "Lname";
	public static final String ADDRESS = "SA";
	public static final String PHONE = "1234";
	public static final String EMAIL = "test@test.com";
	
	public static final String FAILED_MESSAGE_MESSAGE_BODY = "{\"ucid\":55049593,\"brand\":\"CCOM\",\"alertType\":\"BCM\",\"creationDate\":1562058753636,\"alertPayload\":null}";
	

	public static final String METHOD = "POST";
	
	public static final String ACCESS_TOKEN = "787899nkkkhfusfsdfd";
	public static final String REFRESH_TOKEN = "fsdfnsfsldfi97800";
	
	public static final String USER_TYPE = "tenant";
	
	public static final String VALUE = "Value";
	
	public static final Long CUSTOMER_Id = 12345l;
	
	public static BCMCustomer getCustomerObject() {

		BCMCustomer bCMCustomer = new BCMCustomer();
		
	    bCMCustomer.setUcId(UCID);
	    bCMCustomer.setBrand(BRAND_CCOM);
	    bCMCustomer.setAccessToken(ACCESS_TOKEN);
	    bCMCustomer.setRefreshToken(REFRESH_TOKEN);
	    bCMCustomer.setCustEmail(EMAIL);
	    bCMCustomer.setTransactionFinderId(123123l);
	    bCMCustomer.setCustomerDataId(101l);

		return bCMCustomer;

	}

	public static CreateUserRequestModel getCreateUserObject() {

		CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();

		createUserRequestModel.setEmail(EMAIL);
		createUserRequestModel.setType(USER_TYPE);
		createUserRequestModel.setFirstName(FIRST_NAME);
		createUserRequestModel.setLastName(LAST_NAME);
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
	
	public static MatchTransactionRequestModel getMatchTransactionRequestModelObj() {

		MatchTransactionRequestModel matchTransactionRequestModel = new MatchTransactionRequestModel();

		matchTransactionRequestModel.setTransactionID("ojEBr8gApyspayb3EeZ1Uy8dNmyRz6FRe6E6m");
		matchTransactionRequestModel.setAmount("6.33");
		matchTransactionRequestModel.setName("Uber 072515 SF**POOL**");
		matchTransactionRequestModel.setCategoryId("22006001");
		matchTransactionRequestModel.setDate(LocalDate.parse("2019-10-15"));

		return matchTransactionRequestModel;

	}
	
	public static VendorOrdersResponseModel getVendorOrdersResponseModelObj() {

		VendorOrdersResponseModel vendorOrdersResponseModel = new VendorOrdersResponseModel();
		VendorOrdersReportedResponseModel reportedAt = new VendorOrdersReportedResponseModel();
		vendorOrdersResponseModel.setOrderId("123425");
		reportedAt.setEquifax("2019-06-11");
		reportedAt.setTransUnion("2019-06-11");
		reportedAt.setExperian("2019-06-11");
		vendorOrdersResponseModel.setOrdersReportResponse(reportedAt);
		return vendorOrdersResponseModel;

	}
	
	public static Job getNewJobObj() {		
		Job job = new Job();
		job.setJobId(102l);
		job.setBrand("CCOM");
		job.setJobEvents(new ArrayList<>());
		job.getJobEvents().add(new JobEvent(JobEventTypeEnum.CREATED, job));
		job.setJobName(JobNameEnum.RENTTRACK_REPORTED_ORDERS);
		return job;
	}
	
	
	public static JobEvent getJobEventObj() {		
		JobEvent jobEvent =new JobEvent(JobEventTypeEnum.INTERRUPT_REQUESTED, getNewJobObj());	
		return jobEvent;
	}
	
	public static Order getOrderObj() {		
		Order order = new Order();	
		order.setId(101l);
		order.setRtOrderId(4325l);
		List<OrderReportedInfo> orderReportedInfoList =new ArrayList<>();
		orderReportedInfoList.add(new OrderReportedInfo());
		order.setOrderReportedInfoList(orderReportedInfoList);
		return order;
	}
}
