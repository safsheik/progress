package com.progrexion.bcm.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.progrexion.bcm.common.exceptions.BCMModuleException;
import com.progrexion.bcm.common.exceptions.BCMModuleExceptionEnum;
import com.progrexion.bcm.models.v1.ErrorDto;


@ControllerAdvice
public class BCMModuleExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		// javax.validation.* validation errors are handled here
		Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		return ResponseEntity.status(status).body(new ErrorDto(false,
				status.value(),
				BCMModuleExceptionEnum.BCM_API_INVALID_INPUT.getDescription()+fieldErrors.toString(),
				BCMModuleExceptionEnum.BCM_API_INVALID_INPUT.getCode())
				);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// @JsonFormat type of errors are handled here.
		String exMessage=ex.getLocalizedMessage()!=null && ex.getLocalizedMessage().length() != 0? ex.getLocalizedMessage(): "";
		
		return ResponseEntity.status(status).body(new ErrorDto(false,
				status.value(),
				BCMModuleExceptionEnum.BCM_API_INVALID_REQUEST.getDescription()+exMessage.substring(exMessage.lastIndexOf('['), exMessage.lastIndexOf(']')+1),
				BCMModuleExceptionEnum.BCM_API_INVALID_REQUEST.getCode()));
	}

	@ExceptionHandler(BCMModuleException.class)
	public ResponseEntity<ErrorDto> handleBCMModuleException(BCMModuleException e) {
		ErrorDto errorDto = null;
		
		if(StringUtils.isBlank(e.getErrorData()) || e.getErrorMessage().equals(e.getErrorData()))
		{
			errorDto = new ErrorDto(false,e.getHttpStatusCode(),e.getErrorMessage(),e.getErrorCode());
		}
		else
		{
			errorDto = new ErrorDto(false,e.getHttpStatusCode(),e.getErrorMessage()+"-"+e.getErrorData(),e.getErrorCode());
		}
		return ResponseEntity.status(e.getHttpStatusCode()).body(errorDto);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleInternal(final Exception e, final WebRequest request) {
		return handleExceptionInternal(e, createErrorResponse(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				request);
	}

	private Object createErrorResponse() {
		final ErrorDto errorResponse;

		errorResponse = new ErrorDto();
		errorResponse.setErrorCode(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getCode());	
		errorResponse.setError(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getDescription());
		errorResponse.setStatus(false);
		errorResponse.setStatusCode(BCMModuleExceptionEnum.BCM_MODULE_EXCEPTION.getHttpStatusCode());

		return errorResponse;
	}

}
