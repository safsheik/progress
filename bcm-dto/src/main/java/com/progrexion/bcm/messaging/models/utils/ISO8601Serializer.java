package com.progrexion.bcm.messaging.models.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

/**
 * deprecated use Common / common / common-utils -> com.progrexion.common.utils.ISO8601Serializer
 */
//@Deprecated
public class ISO8601Serializer extends StdSerializer<Date> {

	private static final long serialVersionUID = 1L;

	private final DateFormat formatter = new ISO8601DateFormat();

	public ISO8601Serializer() {
		this(null);
	}

	public ISO8601Serializer(Class<Date> t) {
		super(t);
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
		gen.writeString(this.formatter.format(value));
	}
}