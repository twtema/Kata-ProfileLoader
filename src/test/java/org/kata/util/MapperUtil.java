package org.kata.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kata.controller.dto.BankCardDto;
import org.kata.controller.dto.IndividualDto;
import org.kata.entity.BankCard;
import org.kata.entity.Individual;

public class MapperUtil {
    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static IndividualDto deserializeIndividualDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, IndividualDto.class);
    }

    public static Individual deserializeIndividual(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, Individual.class);
    }

    public static BankCardDto deserializeBankCardDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, BankCardDto.class);
    }

    public static BankCard deserializeBankCard(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, BankCard.class);
    }
}