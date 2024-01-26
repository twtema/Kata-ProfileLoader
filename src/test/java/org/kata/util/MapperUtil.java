package org.kata.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.Individual;

public class MapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static IndividualDto deserializeIndividualDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, IndividualDto.class);
    }

    public static Individual deserializeIndividual(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Individual.class);
    }
}