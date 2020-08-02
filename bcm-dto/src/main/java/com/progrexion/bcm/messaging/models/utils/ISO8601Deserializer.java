package com.progrexion.bcm.messaging.models.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * deprecated use Common / common / common-utils -> com.progrexion.common.utils.ISO8601Deserializer
 */
//@Deprecated
public class ISO8601Deserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1L;

	private final DateFormat formatter = new ISO8601DateFormat();

	public ISO8601Deserializer() {
		this(null);
	}

	public ISO8601Deserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		final String date = jsonparser.getText();
		try {
			return this.formatter.parse(date);
		} catch (final ParseException e) {
			throw new RuntimeException(e);
		}
	}

}