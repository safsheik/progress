package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandlordDto implements Serializable{

	private static final long serialVersionUID = 1L;
	@NotEmpty
	private String type;
	private String name;
	private String phone;
	private String email;
	
}
