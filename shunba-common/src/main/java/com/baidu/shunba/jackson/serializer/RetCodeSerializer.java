package com.baidu.shunba.jackson.serializer;

import com.baidu.shunba.bean.RetCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RetCodeSerializer extends JsonSerializer<RetCode> {
    @Override
    public void serialize(RetCode retCode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(retCode.getCode());
    }
}
