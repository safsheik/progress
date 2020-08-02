package com.progrexion.bcm.model.converters;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
	
	@Override
	public Date convertToDatabaseColumn(LocalDate localDt) {
		return (localDt == null ? null : Date.valueOf(localDt));
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDt) {
		return (sqlDt == null ? null : sqlDt.toLocalDate());
	}

}
