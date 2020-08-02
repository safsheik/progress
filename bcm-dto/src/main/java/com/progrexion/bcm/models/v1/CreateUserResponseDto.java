package com.progrexion.bcm.models.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL) 
@Data
public class CreateUserResponseDto {
	
	private String id;	
	private boolean status;

}
