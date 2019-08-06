package com.worksvn.student_service.exceptions.auth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.worksvn.student_service.constants.ResponseValue;
import com.worksvn.student_service.constants.StringConstants;

import java.io.IOException;

public class CustomOAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    protected CustomOAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    public void serialize(CustomOAuth2Exception value,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        ResponseValue responseValue = value.getResponseValue();
        gen.writeNumberField(StringConstants.CODE, responseValue.specialCode());
        gen.writeStringField(StringConstants.MESSAGE, responseValue.message());
        gen.writeObjectField(StringConstants.DATA, null);
        gen.writeEndObject();
    }
}
