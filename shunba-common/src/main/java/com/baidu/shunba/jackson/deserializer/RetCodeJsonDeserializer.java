package com.baidu.shunba.jackson.deserializer;

import com.baidu.shunba.bean.RetCode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class RetCodeJsonDeserializer extends JsonDeserializer<RetCode> {
    @Override
    public RetCode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser == null) {
            return RetCode.BUSINESS_ERROR;
        }

        try {
            return RetCode.fromStringCode(jsonParser.getValueAsString());
        } catch (IOException e) {
        }

        try {
            return RetCode.fromCode(jsonParser.getValueAsInt());
        } catch (IOException e) {
        }

        return RetCode.BUSINESS_ERROR;
    }
}
