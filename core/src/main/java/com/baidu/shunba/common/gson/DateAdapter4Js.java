package com.baidu.shunba.common.gson;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateAdapter4Js implements JsonSerializer<Date>, JsonDeserializer<Date> {

	public JsonElement serialize(Date src, Type arg1, JsonSerializationContext arg2) {
		return new JsonPrimitive(src.getTime() + "");
	}

	public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be a string value");
		}

		return new Timestamp(json.getAsLong());
	}

}
