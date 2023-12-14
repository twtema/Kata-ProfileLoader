package org.kata.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public class Converter<T> {

    private final Class<T> typeParameterClass;

    public Converter(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public T encodeData(String data) throws JsonProcessingException {
        String jsonLike = new String(Base64.getDecoder().decode(data));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonLike, typeParameterClass);
    }

    public String codeData(T data) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonLike = objectMapper.writeValueAsString(data);
        return Base64.getEncoder().encodeToString(jsonLike.getBytes());
    }
}
