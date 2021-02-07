package com.baidu.shunba.autoconfig;

import com.baidu.shunba.bean.RetCode;
import com.baidu.shunba.jackson.serializer.RetCodeSerializer;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class JacksonSerializerDeserializerAutoConfig {
//    @Bean
//    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
//        final String formatString = "yyyy-MM-dd HH:mm:ss";
//        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
//
//        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JavaTimeModule module = new JavaTimeModule();
//
//        //日期序列化、反序列化
//        // module.addSerializer(Date.class, new DateSerializer(true, simpleDateFormat));
//        // module.addDeserializer(Date.class, new DateDeserializers.DateDeserializer());
//
//        // RetCode序列化
//        module.addSerializer(RetCode.class, new RetCodeSerializer());
//
//        objectMapper.registerModule(module);
//        jsonConverter.setObjectMapper(objectMapper);
//        return jsonConverter;
//    }
}
