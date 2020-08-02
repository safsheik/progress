package com.progrexion.bcm.aspects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.progrexion.bcm.common.AppConstants;
import com.progrexion.bcm.common.config.BCMLoadPropOnStartUp;
import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.common.model.ErrorModel;
import com.progrexion.bcm.common.model.WebServiceLogsModel;
import com.progrexion.bcm.services.WebServiceLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class WebServiceLogsAspect {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private WebServiceLogService webServiceLogService;
	
	@Autowired
	private BCMLoadPropOnStartUp startupProperty;

	@Around("@target(classRequestMapping) && @annotation(requestMapping) && within(com.progrexion.bcm.web.controllers..*)")
	public Object saveWebServiceLogs(ProceedingJoinPoint pjp, RequestMapping classRequestMapping,
			RequestMapping requestMapping) throws Throwable {
		Object result = null;
		if(!startupProperty.isLogOn())
		{
			try {
				result = pjp.proceed();
				resetResponse();
			} catch (BCMModuleException rmEx) {
				throw rmEx;
			} catch (Exception ex) {
				throw new BCMModuleException(
						ExceptionUtils.getRootCauseMessage(ex), BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION);
			}
		}
		else
		{
		WebServiceLogsModel webServiceLogsModel = new WebServiceLogsModel();

		try {
			populateRequest(pjp, classRequestMapping, requestMapping, webServiceLogsModel);

		} catch (Exception ex) {
			log.error("Error while setting up Web Service Request", ex);
		}
		

		try {
			result = pjp.proceed();
			resetResponse();
		} catch (BCMModuleException rmEx) {

			populateResponse(rmEx, webServiceLogsModel);
			webServiceLogService.saveWebServiceDetails(webServiceLogsModel);

			throw rmEx;
		} catch (Exception ex) {
			BCMModuleException bcmModuleException = new BCMModuleException(
					ExceptionUtils.getRootCauseMessage(ex), BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION);
			populateResponse(bcmModuleException, webServiceLogsModel);
			webServiceLogService.saveWebServiceDetails(webServiceLogsModel);

			throw bcmModuleException;
		}

		populateResponse(result, webServiceLogsModel);
		webServiceLogService.saveWebServiceDetails(webServiceLogsModel);

		
		}
		return result;
	}

	private void resetResponse() {
		try {
			ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			servletContainer.getResponse().reset();
		} catch (Exception e) {
			// no action required
			log.error(e.getMessage(), e);
		}

	}

	private WebServiceLogsModel populateResponse(Object result, WebServiceLogsModel webServiceLogsModel) {
		Object map = new ModelMapper().map(result, Object.class);
		List<String> methodTypes = startupProperty.getMethodTypeList();
		if(methodTypes.contains(webServiceLogsModel.getHttpMethod()))
		{
			ignoreException(() -> webServiceLogsModel.setResponseBody(AppConstants.DISABLED));
		}
		else {
			ignoreException(() -> webServiceLogsModel.setResponseBody(objectMapper.writeValueAsString(map)));
		}
		
		ignoreException(() -> webServiceLogsModel.setHttStatusCode(HttpStatus.OK.value()));
		return webServiceLogsModel;

	}

	private WebServiceLogsModel populateResponse(BCMModuleException rmEx, WebServiceLogsModel webServiceLogsModel) {
		List<String> methodTypes = startupProperty.getMethodTypeList();
		if(methodTypes.contains(webServiceLogsModel.getHttpMethod()))
		{
			ignoreException(() -> webServiceLogsModel.setResponseBody(AppConstants.DISABLED));
		}
		else {
		ignoreException(() -> webServiceLogsModel
				.setResponseBody(objectMapper.writeValueAsString(new ErrorModel(rmEx.getErrorCode(),
						rmEx.getErrorMessage(), rmEx.getErrorData(), rmEx.getHttpStatusCode()))));
		}
		ignoreException(() -> webServiceLogsModel.setHttStatusCode(rmEx.getHttpStatusCode()));

		return webServiceLogsModel;

	}

	private WebServiceLogsModel populateRequest(ProceedingJoinPoint pjp, RequestMapping classRequestMapping,
			RequestMapping requestMapping, WebServiceLogsModel webServiceLogsModel) {
		ignoreException(() -> webServiceLogsModel.setHttpMethod(requestMapping.method()[0].toString()));
		ignoreException(() -> 
			webServiceLogsModel.setRequestBody(Arrays.stream(pjp.getArgs()).map(value -> {
				String data = StringUtils.EMPTY;
				try {
					data = objectMapper.writeValueAsString(value);
				} catch (Exception e) {
					// No Need to do anything
				}

				return data;
			}).collect(Collectors.joining(";")))
		);
		ignoreException(() -> webServiceLogsModel.setBrand((String) pjp.getArgs()[1]));
		ignoreException(() -> webServiceLogsModel.setUcid((Long) pjp.getArgs()[0]));
		if(requestMapping.value().length==0)
			ignoreException(() -> webServiceLogsModel.setUrl(classRequestMapping.value()[0]));
		else
			ignoreException(() -> webServiceLogsModel.setUrl(classRequestMapping.value()[0] + requestMapping.value()[0]));
		

		return webServiceLogsModel;
	}

	public static void ignoreException(RunnableException r) {
		try {
			r.run();

		} catch (Exception e) {
			// Skip Exception
		}
	}

	 @FunctionalInterface public interface RunnableException
	 {
		 void run() throws Exception;
	}
	
}
