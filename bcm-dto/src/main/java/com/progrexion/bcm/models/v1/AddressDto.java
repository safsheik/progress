package com.progrexion.bcm.models.v1;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) 
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto implements Serializable{

	private static final long serialVersionUID = 1L;
    @NotEmpty
	private String address1;
	private String address2;
	@NotEmpty
	private String city;
	@NotEmpty
	private String zip;
	@NotEmpty
	private String state;
	private String country ;

}
