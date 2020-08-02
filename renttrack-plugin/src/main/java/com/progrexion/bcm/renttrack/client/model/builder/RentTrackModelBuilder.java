package com.progrexion.bcm.renttrack.client.model.builder;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.progrexion.bcm.common.model.vendor.VendorCreateUserRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorLeaseRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorMatchTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorPaymentAccountRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorSubscriptionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorTransactionRequestModel;
import com.progrexion.bcm.common.model.vendor.VendorUtilityStatusRequestModel;
import com.progrexion.bcm.common.properties.RentTrackConfigProperties;
import com.progrexion.bcm.renttrack.client.model.LeaseApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.MatchTransactionArrayApiModel;
import com.progrexion.bcm.renttrack.client.model.PaymentAccountApiModel;
import com.progrexion.bcm.renttrack.client.model.RTCreateUserRequestModel;
import com.progrexion.bcm.renttrack.client.model.SubscriptionApiModel;
import com.progrexion.bcm.renttrack.client.model.TransactionApiModel;
import com.progrexion.bcm.renttrack.client.model.UtilityStatusApiModel;

@Component
public class RentTrackModelBuilder {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RentTrackConfigProperties property;

	public SubscriptionApiModel buildCreateSubscriptionApiModel(
			VendorSubscriptionRequestModel vendorsubscriptionRequestModel) {
		SubscriptionApiModel subscriptionApiModel = new SubscriptionApiModel();
		subscriptionApiModel.setPromotionCode(vendorsubscriptionRequestModel.getPromotionCode());
		subscriptionApiModel.setPlanName(vendorsubscriptionRequestModel.getPlan());
		return subscriptionApiModel;
	}
	public RTCreateUserRequestModel createUserAccountApiRequestModel(VendorCreateUserRequestModel vendorRequestModel) {
		return new ModelMapper().map(vendorRequestModel, RTCreateUserRequestModel.class);
	}

	public LeaseApiModel buildLeaseApiModel(VendorLeaseRequestModel request) {

		return new ModelMapper().map(request, LeaseApiModel.class);
	}

	public LeaseApiModel updateLeaseApiModel(VendorLeaseRequestModel request) {
		return new ModelMapper().map(request, LeaseApiModel.class);
	}

	public PaymentAccountApiModel buildCreatePaymentAccountApiModel(VendorPaymentAccountRequestModel request) {
		return modelMapper.map(request, PaymentAccountApiModel.class);
	}

	public TransactionApiModel buildTransactionApiModel(VendorTransactionRequestModel request) {
		TransactionApiModel transFinderApiModel = new TransactionApiModel();

		transFinderApiModel.setLeaseId(request.getLeaseId());
		transFinderApiModel.setTransactionFinderId(request.getTransactionFinderId());
		transFinderApiModel.setAmountMax(minMaxAmountCalculate(request.getRentAmount(), true));
		transFinderApiModel.setAmountMin(minMaxAmountCalculate(request.getRentAmount(), false));
		transFinderApiModel.setWindowClose(openCloseDaysCalculate(request.getDueDay(), true));
		transFinderApiModel.setWindowOpen(openCloseDaysCalculate(request.getDueDay(), false));
		transFinderApiModel.setLeaseUrl(request.getLeaseUrl());
		transFinderApiModel.setPaymentAccountUrl(request.getPaymentAccountUrl());

		return transFinderApiModel;
	}

	// Calculate amount_min || amount_max
	private float minMaxAmountCalculate(float rentAmount, boolean isTrue) {

		float percentageAmount;
		float rtAmount;

		if (isTrue) {
			percentageAmount = (property.getTransactionAmountMaxBy() / 100) * rentAmount;

			rtAmount = rentAmount + percentageAmount;

		} else {
			percentageAmount = (property.getTransactionAmountMinBy() / 100) * rentAmount;

			rtAmount = rentAmount - percentageAmount;
		}

		return rtAmount;
	}

	// Calculate Open and Close Days
	  private int openCloseDaysCalculate(int dueDay, boolean isTrue) {

	    int ocDay;
	    int dueDaysReturn;

	    if (isTrue) {
	      ocDay = dueDay + property.getTransactionWindowCloseBy();

	    } else {
	      ocDay = dueDay - property.getTransactionWindowOpenBy();
	    }
	    dueDaysReturn = validateDays(ocDay, isTrue);

	    return dueDaysReturn;
	  }

	  private int validateDays(int ocDays, boolean isTrue) {
	    int dueDays = ocDays;
	    int maxDueDay = 31;

	    if (isTrue) {

	      if (ocDays > maxDueDay) {

	        dueDays = ocDays - maxDueDay;

	      }

	    } else {

	      if (ocDays < 1) {

	        dueDays = maxDueDay + ocDays;

	      }

	    }

	    return dueDays;

	  }

	public MatchTransactionArrayApiModel buildMatchTrxApiModel(VendorMatchTransactionRequestModel request) {
		MatchTransactionApiModel transactions = new ModelMapper().map(request, MatchTransactionApiModel.class);
		MatchTransactionApiModel[] requestArray=new MatchTransactionApiModel[1];
		requestArray[0] = transactions;
		MatchTransactionArrayApiModel matchTransactionArrayApiModel = new MatchTransactionArrayApiModel();
		matchTransactionArrayApiModel.setTransactions(requestArray);
		return matchTransactionArrayApiModel;
				
	}
	
	public TransactionApiModel buildSearchTransactionApiModel(Long transactionFinderId) {
		TransactionApiModel transFinderApiModel = new TransactionApiModel();

		transFinderApiModel.setTransactionFinderId(transactionFinderId);
		transFinderApiModel.setAmountMax(property.getTrxByDefaultAmountMax());
		transFinderApiModel.setAmountMin(property.getTrxByDefaultAmountMin());
		transFinderApiModel.setWindowClose(property.getTrxByDefaultWindowClose());
		transFinderApiModel.setWindowOpen(property.getTrxByDefaultWindowOpen());

		return transFinderApiModel;
	}

	public UtilityStatusApiModel buildUtiltyStatusApiModel(VendorUtilityStatusRequestModel request) {
		return modelMapper.map(request, UtilityStatusApiModel.class);
	}
	
	public TransactionApiModel buildTransactionUpdateApiModel(VendorTransactionRequestModel request) {
		TransactionApiModel transFinderApiModel = new TransactionApiModel();

		transFinderApiModel.setAmountMax(minMaxAmountCalculate(request.getRentAmount(), true));
		transFinderApiModel.setAmountMin(minMaxAmountCalculate(request.getRentAmount(), false));
		transFinderApiModel.setWindowClose(openCloseDaysCalculate(request.getDueDay(), true));
		transFinderApiModel.setWindowOpen(openCloseDaysCalculate(request.getDueDay(), false));
		transFinderApiModel.setPaymentAccountUrl(request.getPaymentAccountUrl());

		return transFinderApiModel;
	}

}
